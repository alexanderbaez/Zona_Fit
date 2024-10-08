package ab.zona_fit;

import ab.zona_fit.gui.ZonaFitForma;
import com.formdev.flatlaf.FlatDarculaLaf;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
//@SpringBootApplication
public class ZonaFitSwing{

    public static void main(String[] args) {
        //configuramos el modo oscuro - configuramos en pom, la <dependency>F
        FlatDarculaLaf.setup();
        //intanciamos la fabrica de spring
        ConfigurableApplicationContext contextoSpring = new SpringApplicationBuilder(ZonaFitSwing.class)
                .headless(false).web(WebApplicationType.NONE).run(args);


        //crear un objeto de Swing
        SwingUtilities.invokeLater(()-> {
           ZonaFitForma zonaFitForma = contextoSpring.getBean(ZonaFitForma.class);
           zonaFitForma.setVisible(true);
        });
    }
}
