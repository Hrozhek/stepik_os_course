package com.github.hrozhek.stepikoscourse.week2.data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryModel {

    private final long r;

    private final Map<BigInteger, BigInteger> physicalAddressesToValue = new ConcurrentHashMap<>();
    private final List<BigInteger> logicalAddresses = new ArrayList<>();

    public MemoryModel(long r) {
        this.r = r;
    }

    public void mapPhysicalAddressToValue(BigInteger physicalAddress, BigInteger value) {
        physicalAddressesToValue.put(physicalAddress, value);
    }

    public void addLogicalAdress(BigInteger logicalAddress) {
        logicalAddresses.add(logicalAddress);
    }

    public long getR() {
        return r;
    }

    public Map<BigInteger, BigInteger> getPhysicalAddressesToValue() {
        return new HashMap<BigInteger, BigInteger>(physicalAddressesToValue);
    }

    public List<BigInteger> getLogicalAddresses() {
        return new ArrayList<BigInteger>(logicalAddresses);
    }
}
