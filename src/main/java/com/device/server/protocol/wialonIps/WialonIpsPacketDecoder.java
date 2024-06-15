package com.device.server.protocol.wialonIps;

import com.device.server.model.Telemetry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.device.server.protocol.wialonIps.WialonIpsUtils.dmsToDecimal;

public class WialonIpsPacketDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(WialonIpsPacketDecoder.class);

    public static final int VERSION_1_1_L_LENGTH = 2;
    public static final int VERSION_2_0_L_LENGTH = 4;

    public static final int DATA_PACKET_SHORT_SIZE = 10;
    public static final int DATA_PACKET_FULL_SIZE = 16;

    public static final int DATA_DATE_INDEX = 0;
    public static final int DATA_TIME_INDEX = 1;
    public static final int DATA_LAT_INDEX = 2;
    public static final int DATA_LAT_HEMISPHERE_INDEX = 3;
    public static final int DATA_LON_INDEX = 4;
    public static final int DATA_LON_HEMISPHERE_INDEX = 5;
    public static final int DATA_SPEED_INDEX = 6;
    public static final int DATA_COURSE_INDEX = 7;
    public static final int DATA_ALTITUDE_INDEX = 8;
    public static final int DATA_SATELLITES_COUNT_INDEX = 9;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("ddMMyy HHmmss").withZone(ZoneId.of("UTC"));

    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        final int length = in.toString(StandardCharsets.US_ASCII).length();
        final String input = in.readCharSequence(length, StandardCharsets.US_ASCII).toString().replaceAll("\r\n", "");

        final WialonIpsPacket packet = new WialonIpsPacket(WialonIpsUtils.WialonIpsPacketType.determineType(input));
        final String[] records;
        switch (packet.getType()) {
            case L:
                records = input.split("#")[2].split(";");
                if (records.length == VERSION_1_1_L_LENGTH) {
                    packet.setIdentifier(records[0]);
                } else if (records.length == VERSION_2_0_L_LENGTH) {
                    packet.setIdentifier(records[1]);
                }
                break;

            case SD:
                records = input.split("#")[2].split(";");
                packet.getRecords().add(
                        parseShortData(records).build());
                break;

            case D:
                records = input.split("#")[2].split(";");
                packet.getRecords().add(
                        parseFullData(records).build());
                break;

            case B:
                records = input.split("#")[2].split("\\|");
                for (final String record : records) {
                    final String[] data = record.split(";");
                    if (data.length == DATA_PACKET_SHORT_SIZE) {
                        packet.getRecords().add(
                                parseShortData(data).build());
                    } else if (data.length == DATA_PACKET_FULL_SIZE) {
                        packet.getRecords().add(
                                parseFullData(data).build());
                    }
                }
                break;

            case P:
                break;

            case M:
                break;

            default:
                throw new IllegalArgumentException();
        }

        out.add(packet);
    }

    public Telemetry.Builder parseShortData(final String[] records) {
        final Telemetry.Builder builder = Telemetry.builder();

        if (StringUtils.isNumeric(records[DATA_DATE_INDEX])
                && StringUtils.isNumeric(records[DATA_TIME_INDEX])) {

            try {
                final ZonedDateTime zonedDateTime = ZonedDateTime.from(
                        DATE_TIME_FORMATTER.parse(records[DATA_DATE_INDEX] + " " + records[DATA_TIME_INDEX]));
                builder.withFixTime(Instant.from(zonedDateTime));
            } catch (Exception ex) {
                return builder;
            }

        } else {
            builder.withFixTime(Instant.now());
        }

        builder.withLatitude(NumberUtils.isCreatable(records[DATA_LAT_INDEX])
                ? dmsToDecimal(Double.parseDouble(records[DATA_LAT_INDEX]), records[DATA_LAT_HEMISPHERE_INDEX])
                : null);
        builder.withLongitude(NumberUtils.isCreatable(records[DATA_LON_INDEX])
                ? dmsToDecimal(Double.parseDouble(records[DATA_LON_INDEX]), records[DATA_LON_HEMISPHERE_INDEX])
                : null);
        builder.withSpeed(StringUtils.isNumeric(records[DATA_SPEED_INDEX])
                ? Short.parseShort(records[DATA_SPEED_INDEX])
                : null);
        builder.withCourse(StringUtils.isNumeric(records[DATA_COURSE_INDEX])
                ? Short.parseShort(records[DATA_COURSE_INDEX])
                : null);
        builder.withAltitude(StringUtils.isNumeric(records[DATA_ALTITUDE_INDEX])
                ? Short.parseShort(records[DATA_ALTITUDE_INDEX])
                : null);
        builder.withSatellitesCount(StringUtils.isNumeric(records[DATA_SATELLITES_COUNT_INDEX])
                ? Short.parseShort(records[DATA_SATELLITES_COUNT_INDEX])
                : null);

        return builder;
    }

    public Telemetry.Builder parseFullData(final String[] records) {
        //TODO
        return parseShortData(records);
    }
}
