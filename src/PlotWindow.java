
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


class ProcessD{
    int processId;
    int start;
    int end;

    ProcessD(int start,int end,int processId){
        this.processId = processId;
        this.start = start;
        this.end = end;
    }
}

public class PlotWindow extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    static class MyToolTipGenerator extends IntervalCategoryToolTipGenerator {


        @Override
        public String generateToolTip(CategoryDataset cds, int row, int col) {
            final String s = super.generateToolTip(cds, row, col);
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }

    StringBuilder message = new StringBuilder();
    String type = "ERROR";

    public PlotWindow(final String title, List<Integer> processes) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int averageWait = 0;
        int averageTurnaround = 0;
        // HashMap<Integer, Boolean> occurred = new HashMap<>();
        // for (duration Duration : processes) {
        //     if (occurred.get(Duration.id) == null) {
        //         int wait = Duration.start - Duration.arrivalTime;
        //         averageWait += wait;
        //         averageTurnaround += (wait + Duration.burstTime);
        //         message.append(MessageFormat.format("{0} ,Waiting Time :{1} ,Turnaround Time :{2}\n", Duration.name, wait, wait + Duration.burstTime));
        //         occurred.put(Duration.id, true);
        //     }
        // }
        message.append(MessageFormat.format("The average waiting is : {0}\n", averageWait / processes.size()));
        message.append(MessageFormat.format("The average Turnaround is : {0}\n", averageTurnaround / processes.size()));

        GanttCategoryDataset dataset = createDataset(processes);
        JFreeChart chart = createChart(dataset);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(-2);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(500, 300));
        setContentPane(chartPanel);


      
    }

    public GanttCategoryDataset createDataset(List<Integer> processes) {
        HashMap<Integer, Task> tasks = new HashMap<>();

        for (Integer process : processes) {
            // message.append(MessageFormat.format("{0} {1} {2} {3}\n", process.name, process.start, process.end, process.description));
        }

        List<ProcessD> processTime = new ArrayList<>();
       
        int current = processes.get(0);
        int start = 0;

        for (int i = 1; i < processes.size(); i++) {
            if ( processes.get(i) != current) {
                 ProcessD d = new ProcessD(start, i,processes.get(i-1).intValue());
                processTime.add(d);
               
                
                current =  processes.get(i);
                start = i;
            }
        }

        // Add the last segment
       
          ProcessD d = new ProcessD(start, processes.size()-1,processes.get(processes.size()-1));
            processTime.add(d);

        


        for (ProcessD process : processTime) {
            if (tasks.get(process.processId) == null) {
                tasks.put(process.processId,  new Task("P"+process.processId, new SimpleTimePeriod(0, processTime.get(processTime.size() - 1).end)));
            }
            System.out.println("Process ID: "+process.processId+" start Time: "+process.start+" endTime : " + process.end);
            Task t = new Task("P"+process.processId, new SimpleTimePeriod(process.start, process.end));
            t.setDescription("P"+process.processId);
            if (tasks.get(process.processId) != null) {
                tasks.get(process.processId).addSubtask(t);
                
            }
        }

        TaskSeriesCollection collection = new TaskSeriesCollection();
        tasks.forEach((K, V) -> {
            TaskSeries t = new TaskSeries(V.getDescription());
            t.add(V);
            collection.add(t);
        });
        return collection;
    }

    private JFreeChart createChart(GanttCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
                "CPU Usage",
                "processes",
                "Time",
                dataset,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        plot.getRenderer().setDefaultToolTipGenerator(new MyToolTipGenerator());

        DateAxis axis = (DateAxis) plot.getRangeAxis();

        axis.setDateFormatOverride(new SimpleDateFormat("S"));
        return chart;
    }

}