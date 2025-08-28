package biz.robotdawn.indicators.bookmap;

import velox.api.layer1.annotations.Layer1ApiVersion;
import velox.api.layer1.annotations.Layer1ApiVersionValue;
import velox.api.layer1.annotations.Layer1SimpleAttachable;
import velox.api.layer1.annotations.Layer1StrategyName;
import velox.api.layer1.data.InstrumentInfo;
import velox.api.layer1.layers.utils.OrderBook;
import velox.api.layer1.messages.indicators.Layer1ApiUserMessageModifyIndicator;
import velox.api.layer1.simplified.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@Layer1SimpleAttachable
@Layer1StrategyName("Moving Average Backfilled Parameters")
@Layer1ApiVersion(Layer1ApiVersionValue.VERSION2)

public class MovingAverageBackfilledParameters implements CustomModule, BarDataListener, BackfilledDataListener {
    Indicator indicator;
    long intervalLength = Intervals.INTERVAL_5_SECONDS;

    @Parameter(name="Number of Intervals", minimum = 3, maximum = 200)
    Integer n = 10; //number of intervals

    List<Double> prices = new LinkedList<>();

    @Override
    public void onBar(OrderBook orderBook, Bar bar) {
        prices.add(bar.getClose());

        while(prices.size() > n) {
            prices.remove(0);
        }

        double sum = 0.0;
        int intervals = 0;

        for(double price : prices) {
            if (!Double.isNaN(price)) {
                sum += price;
                intervals++;
            }
        }

        double average = sum / intervals;
        indicator.addPoint(average);  //drawing

    }

    @Override
    public void initialize(String s, InstrumentInfo instrumentInfo, Api api, InitialState initialState) {
        indicator = api.registerIndicator("mov avg backfilled parameters", Layer1ApiUserMessageModifyIndicator.GraphType.PRIMARY);
        indicator.setColor(Color.ORANGE);
    }

    @Override
    public void stop() {

    }

    @Override
    public long getInterval() {
        return intervalLength;
    }
}
