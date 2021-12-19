package animalSimulation.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

public class NumberTextField extends TextField {
    private final IntegerProperty intProperty;

    public NumberTextField(int defaultVal) {
        this.intProperty = new SimpleIntegerProperty();

        TextFormatter<Number> formatter = new TextFormatter<>(
                new NumberStringConverter(),
                defaultVal,
                new IntegerFilter()
        );

        formatter.valueProperty().bindBidirectional(this.intProperty);
        this.setTextFormatter(formatter);
    }

    public int getValue() {
        return this.intProperty.getValue();
    }
}
