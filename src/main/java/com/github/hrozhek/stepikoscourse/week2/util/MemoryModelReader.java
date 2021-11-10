package com.github.hrozhek.stepikoscourse.week2.util;

import com.github.hrozhek.stepikoscourse.week2.data.MemoryModel;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MemoryModelReader {

    public static MemoryModel read(Path path) throws IOException {
        int index = 0;
        List<String> lines = Files.readAllLines(path);
        String[] firstLine = lines.get(index++).split(" ");

        long m = Long.parseLong(firstLine[0]);
        long q = Long.parseLong(firstLine[1]);
        long r = Long.parseLong(firstLine[2]);
        MemoryModel model = new MemoryModel(r);

        //todo - validate numbers
        for (; m-- > 0; index++) {
            String[] mLine = lines.get(index).split(" ");
            BigInteger paddr = new BigInteger(mLine[0]);
            BigInteger value = new BigInteger(mLine[1]);
            model.mapPhysicalAddressToValue(paddr, value);
        }
        for (; q-- > 0; index++) {
            model.addLogicalAdress(new BigInteger(lines.get(index)));
        }
        return model;
    }
}
