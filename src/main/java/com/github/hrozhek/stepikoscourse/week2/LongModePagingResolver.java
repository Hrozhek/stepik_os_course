package com.github.hrozhek.stepikoscourse.week2;

import com.github.hrozhek.stepikoscourse.week2.data.LogicalAddressModel;
import com.github.hrozhek.stepikoscourse.week2.data.MemoryModel;
import com.github.hrozhek.stepikoscourse.week2.exception.AddressException;
import com.github.hrozhek.stepikoscourse.week2.exception.HighBitsException;
import com.github.hrozhek.stepikoscourse.week2.exception.NonValidPageException;
import com.github.hrozhek.stepikoscourse.week2.util.MemoryModelReader;
import com.github.hrozhek.stepikoscourse.week2.util.ResourceGetter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LongModePagingResolver {

    private static final String FAULT = "fault";

    private static final BigInteger OFFSET_MASK = BigInteger.valueOf(0b1111_11111111);
    private static final BigInteger PAGE_MASK = BigInteger.valueOf(0b1_11111111);

    private static final BigInteger ADDRESS_MASK = BigInteger.valueOf(0b11111111_11111111_11111111_11111111_11111111L);

    private static final BigInteger RECORD_BYTES = BigInteger.valueOf(8L);

    private static final BigInteger HIGH_BITS_EQUALS_LAST_ONE = BigInteger.valueOf(0b11111111_11111111);
    private static final BigInteger HIGH_BITS_EQUALS_LAST_ZERO = BigInteger.ZERO;

    private static final byte[] EOL = "\n".getBytes();

    public static void main(String[] args) throws IOException, URISyntaxException {
        Path path = ResourceGetter.getResourceAsPath("simplecase.txt");
        MemoryModel model = MemoryModelReader.read(path);
        //answer will be in the target!!!
        File answer = Paths.get(path.getParent().toString(), "answer.txt").toFile();
        if (answer.exists()) {
            answer.delete();
        }
        answer.createNewFile();
        List<String> answerAsList = mapModelToPhysicalAddress(model);
        try (OutputStream os = new FileOutputStream(answer)) {
            answerAsList.stream().map(String::getBytes).forEach(s -> {
                try {
                    os.write(s);
                    os.write(EOL);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        answerAsList.forEach(System.out::println);
    }

    private static List<String> mapModelToPhysicalAddress(MemoryModel model) {
        List<String> result = new ArrayList<>();
        List<BigInteger> logicalAddresses = model.getLogicalAddresses();
        Map<BigInteger, BigInteger> physicalAddresses = model.getPhysicalAddressesToValue();
        long physicalStart = model.getR();
        for (BigInteger logicalAddress: logicalAddresses) {
            try {
                checkHighBits(logicalAddress);
                LogicalAddressModel logicalAddressModel = resolveLogicalAddress(logicalAddress);
                result.add(resolveAddress(logicalAddressModel, physicalAddresses, physicalStart));
            } catch (AddressException e) {
                result.add(FAULT);
            }
        }
        return result;
    }

    private static void checkHighBits(BigInteger logicalAddress) throws HighBitsException {
        //check that 16 high bits equals to 47's bit
        logicalAddress = logicalAddress.shiftRight(48);
        if (logicalAddress.compareTo(HIGH_BITS_EQUALS_LAST_ZERO) != 0
                && logicalAddress.compareTo(HIGH_BITS_EQUALS_LAST_ONE) != 0) {
            throw new HighBitsException();
        }
    }


    private static LogicalAddressModel resolveLogicalAddress(BigInteger logicalAddress) {
        int p1 = logicalAddress.shiftRight(12).and(PAGE_MASK).intValue();
        int p2 = logicalAddress.shiftRight(21).and(PAGE_MASK).intValue();
        int p3 = logicalAddress.shiftRight(30).and(PAGE_MASK).intValue();
        int p4 = logicalAddress.shiftRight(39).and(PAGE_MASK).intValue();
        int offset = logicalAddress.and(OFFSET_MASK).intValue();
        return new LogicalAddressModel(p1, p2, p3, p4, offset);
    }

    private static String resolveAddress(LogicalAddressModel model, Map<BigInteger, BigInteger> physicalAddresses, long physicalStart) {
        BigInteger address = doStep(BigInteger.valueOf(physicalStart), model.getPageFour(), physicalAddresses);
        address = doStep(address, model.getPageThree(), physicalAddresses);
        address = doStep(address, model.getPageTwo(), physicalAddresses);
        address = doStep(address, model.getPageOne(), physicalAddresses);
        return address.add(BigInteger.valueOf(model.getOffset())).toString();
    }

    private static BigInteger doStep(BigInteger start, int part, Map<BigInteger, BigInteger> physicalAddresses) {
        BigInteger physicalAddress = start.add(BigInteger.valueOf(part).multiply(RECORD_BYTES));
        BigInteger value = physicalAddresses.get(physicalAddress);
        if (value == null || !checkLastBitIsOne(value)) {
            throw new NonValidPageException();
        }

        return extractAddressFromRecord(value);
    }

    private static boolean checkLastBitIsOne(BigInteger value) {
        return value.divideAndRemainder(BigInteger.valueOf(2L))[1].equals(BigInteger.ONE);
    }

    private static BigInteger extractAddressFromRecord(BigInteger value) {
        return value.shiftRight(12).and(ADDRESS_MASK).shiftLeft(12);
    }

}
