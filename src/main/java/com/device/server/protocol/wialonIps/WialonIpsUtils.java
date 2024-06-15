package com.device.server.protocol.wialonIps;

public enum WialonIpsUtils {;

    public enum WialonIpsPacketType {


        L,
        AL,
        D,
        AD,
        P,
        AP,
        SD,
        ASD,
        B,
        AB,
        M,
        AM,
        QI,
        I,
        AI,
        QT,
        IT,
        AIT,
        T,
        AT,
        US,
        UC;

        public static WialonIpsPacketType determineType(final String input) {
            final String[] split = input.split("#");
            return WialonIpsPacketType.valueOf(split[1]);
        }
    }

    public static Double dmsToDecimal(final Double coordinate, final String hemisphere) {
        final int degrees = (int) (coordinate / 100.0);
        final double minutes = coordinate % 100.0;
        final double decimal = degrees + (minutes / 60.0);
        if ("N".equals(hemisphere) || "E".equals(hemisphere)) {
            return decimal;
        } else {
            return -decimal;
        }
    }

    public static String sanitize(final String value){
        return value.replaceAll("\u0000", "");
    }
}
