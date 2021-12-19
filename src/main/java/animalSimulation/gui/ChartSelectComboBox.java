package animalSimulation.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;


public class ChartSelectComboBox extends ComboBox<String> {
    private final StatisticsChart chart;

    public ChartSelectComboBox(StatisticsChart chart) {
        this.chart = chart;
        ObservableList<String> nameList = FXCollections.observableArrayList(chart.plottable);
        this.setItems(nameList);
        this.setPrefWidth(200);

//        this.setOnAction((event) -> {
//            this.chart.setActiveChart(this.getValue());
//        });

        this.valueProperty().addListener(((observable, oldValue, newValue) -> {
            this.chart.setActiveChart(newValue);
        }));
    }
}
