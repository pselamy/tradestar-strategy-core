package com.verlumen.tradestar.core.tradehistory;

import com.google.protobuf.Timestamp;
import com.verlumen.tradestar.protos.candles.Candle;
import com.verlumen.tradestar.protos.candles.Granularity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static java.lang.Math.random;
import static java.time.Instant.ofEpochSecond;
import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.ta4j.core.num.DecimalNum.valueOf;

public class BarFactoryTest {
    // Mon, 21 Feb 2022 23:21:05 GMT
    private static final long START_IN_SECONDS = 1645485664L;
    private static final Instant START_INSTANT = ofEpochSecond(START_IN_SECONDS);
    private static final double OPEN = random();
    private static final double HIGH = random();
    private static final double LOW = random();
    private static final double CLOSE = random();
    private static final double VOLUME = random();

    private static final Candle CANDLE = Candle.newBuilder()
            .setStart(Timestamp.newBuilder().setSeconds(START_IN_SECONDS))
            .setOpen(OPEN)
            .setHigh(HIGH)
            .setLow(LOW)
            .setClose(CLOSE)
            .setVolume(VOLUME)
            .build();
    private static final BaseBarBuilder baseBarBuilder = BaseBar.builder();

    @BeforeEach
    void setup() {
        baseBarBuilder
                .openPrice(valueOf(OPEN))
                .highPrice(valueOf(HIGH))
                .lowPrice(valueOf(LOW))
                .closePrice(valueOf(CLOSE))
                .volume(valueOf(VOLUME));
    }

    @ParameterizedTest
    @EnumSource(ThrowsIllegalArgumentExceptionTestCase.class)
    void create_withMalformedCandle_throwsIllegalArgumentException(
            ThrowsIllegalArgumentExceptionTestCase testCase) {
        assertThrows(IllegalArgumentException.class,
                () -> BarFactory.create(testCase.candle));
    }

    @ParameterizedTest
    @EnumSource(Granularity.class)
    void create_withSpecifiedGranularity_returnsNewBar(
            Granularity granularity) {
        assume().that(granularity).isNotEqualTo(Granularity.UNSPECIFIED);
        Candle candle = CANDLE.toBuilder().setGranularity(granularity).build();
        Duration timePeriod = GranularitySpec.get(granularity).duration();
        ZonedDateTime endTime = START_INSTANT.plus(timePeriod).atZone(UTC);
        Bar expected = baseBarBuilder.timePeriod(timePeriod).endTime(endTime).build();

        Bar actual = BarFactory.create(candle);

        assertThat(actual).isEqualTo(expected);
    }

    private enum ThrowsIllegalArgumentExceptionTestCase {
        MISSING_GRANULARITY(CANDLE.toBuilder()
                .setGranularity(Granularity.UNSPECIFIED)
                .build()),
        MISSING_START(CANDLE.toBuilder().clearStart().build()),
        MISSING_OPEN(CANDLE.toBuilder().clearOpen().build()),
        MISSING_HIGH(CANDLE.toBuilder().clearHigh().build()),
        MISSING_LOW(CANDLE.toBuilder().clearLow().build()),
        MISSING_CLOSE(CANDLE.toBuilder().clearClose().build()),
        MISSING_VOLUME(CANDLE.toBuilder().clearVolume().build());

        private final Candle candle;

        ThrowsIllegalArgumentExceptionTestCase(Candle candle) {
            this.candle = candle;
        }
    }
}
