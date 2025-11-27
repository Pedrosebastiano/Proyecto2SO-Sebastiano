/*
 * Panel para mostrar estadísticas gráficas del Buffer Cache
 */
package GUI;
/**
 *
 * @author PedroSeb
 */
import SISTEMA.BufferCache;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;

public class BufferStatsPanel extends JPanel {
    private BufferCache buffer;
    private ChartPanel hitMissChartPanel;
    private ChartPanel hitRateChartPanel;
    private ChartPanel capacityChartPanel;

    public BufferStatsPanel(BufferCache buffer) {
        this.buffer = buffer;
        setLayout(new GridLayout(2, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        inicializarGraficos();
    }

    private void inicializarGraficos() {
    
        
        hitRateChartPanel = crearGraficoHitRate();
        hitRateChartPanel.setPreferredSize(new Dimension(400, 300));
        
        capacityChartPanel = crearGraficoCapacidad();
        capacityChartPanel.setPreferredSize(new Dimension(400, 1000));
        
       
        
        add(hitRateChartPanel);
        add(capacityChartPanel);
    }

    

    private ChartPanel crearGraficoHitRate() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        int hits = buffer.getHits();
        int misses = buffer.getMisses();
        int total = hits + misses;
        
        if (total > 0) {
            dataset.setValue("Hits (" + String.format("%.1f%%", buffer.getHitRate()) + ")", hits);
            dataset.setValue("Misses (" + String.format("%.1f%%", 100 - buffer.getHitRate()) + ")", misses);
        } else {
            dataset.setValue("Sin datos", 1);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                " Tasa de Aciertos",
                dataset,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.green);
        
        return new ChartPanel(chart);
    }

    private ChartPanel crearGraficoCapacidad() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(buffer.getSize(), "Bloques", "En Uso");
        dataset.addValue(buffer.getCapacity() - buffer.getSize(), "Bloques", "Libre");

        JFreeChart chart = ChartFactory.createBarChart(
                "Uso del Buffer",
                "Estado",
                "Bloques",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        chart.setBackgroundPaint(Color.green);
        
       
        org.jfree.chart.plot.CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(240, 240, 240));
        
        // Crear un renderer personalizado para colorear por categoría
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                if (column == 0) {
                    return new Color(220, 53, 69); 
                } else {
                    return new Color(0, 123, 255);  
                }
            }
        };
        
        plot.setRenderer(renderer);
        
        return new ChartPanel(chart);
    }

    
    public void actualizarGraficos() {
        removeAll();
        inicializarGraficos();
        revalidate();
        repaint();
    }
}