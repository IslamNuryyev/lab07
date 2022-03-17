package lab07;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class Main extends Application {

    private static Color[] pieColours = {  
        Color.AQUA, Color.GOLD, Color.DARKORANGE,  
        Color.DARKSALMON, Color.LAWNGREEN, Color.PLUM 
    }; 
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = null;
        canvas = new Canvas();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Map<String, Integer> numWarningType = loadCSV("resources/weatherwarnings-2015.csv", 5);
        List<Integer> counts = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (String key : numWarningType.keySet()) {
            int count = numWarningType.get(key);

            labels.add(key);
            counts.add(count);
        }

        Group root = new Group();
        primaryStage.setTitle("Lab07");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();

        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());

        root.getChildren().add(canvas);

        drawPieChart(counts, gc);
        drawLegend(labels, gc);
    }

    private void countWarning(Map<String, Integer> numWarningType, String warningType) {
        if (numWarningType.containsKey(warningType)) {
            int oldCount = numWarningType.get(warningType);
            numWarningType.put(warningType, oldCount + 1);
        } else {
            numWarningType.put(warningType, 1);
        }
    }

    private Map<String, Integer> loadCSV(String filepath, int columnIndex) {
        Map<String, Integer> numWarningType = new TreeMap<>();

        File inFile = new File(filepath);

        try {
            if (inFile.exists()) {
                BufferedReader input = new BufferedReader(new FileReader(inFile));

                String line;
                while((line = input.readLine()) != null) {
                    String[] cells = line.split(",");
                    countWarning(numWarningType, cells[columnIndex]);
                }

                input.close();
            } else {
                System.out.println("Error: File not found.");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numWarningType;
        
    }

    private void drawPieChart(List<Integer> numWarningType, GraphicsContext gc) {
        int total = 0;

        for (int i = 0; i < numWarningType.size(); i++) {
            total += numWarningType.get(i);
        }

        double startAngle = 0.0;

        for (int i = 0; i < numWarningType.size(); i++) {
            double percent = (double)numWarningType.get(i) / (double)total;
            double sweepAngle = percent * 360.0;

            gc.setFill(pieColours[i]);
            gc.fillArc(550, 100, 400, 400, startAngle, sweepAngle, ArcType.ROUND);

            startAngle += sweepAngle;
        }
    }

    private void drawLegend(List<String> labels, GraphicsContext gc) {
        int width = 30;
        int height = 20;
        int rowHeight = 40;
        int x = 50;
        int y = 100;

        for (int i = 0; i < labels.size(); i++) {
            gc.setFill(pieColours[i]);
            gc.fillRect(x, y, width, height);

            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height);

            gc.setFill(Color.BLACK);
            gc.fillText(labels.get(i), x + 40, y + 15);

            y += rowHeight;

        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    
}
