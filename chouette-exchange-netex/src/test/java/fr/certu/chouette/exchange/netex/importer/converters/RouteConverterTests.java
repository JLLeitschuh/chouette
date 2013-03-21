package fr.certu.chouette.exchange.netex.importer.converters;

import com.vividsolutions.jts.util.Assert;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import fr.certu.chouette.model.neptune.Route;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.FileUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


@ContextConfiguration(locations = {"classpath:testContext.xml"})
@SuppressWarnings("unchecked")
public class RouteConverterTests extends AbstractTestNGSpringContextTests {

    private RouteConverter routeConverter;

    @BeforeClass
    protected void setUp() throws Exception {
        File f = FileUtils.getFile("src","test", "resources", "line_test.xml");;
        FileInputStream fis = new FileInputStream(f);
        byte[] b = new byte[(int) f.length()];
        fis.read(b);
        
        VTDGen vg = new VTDGen();
        vg.setDoc(b);
        vg.parse(true); // set namespace awareness to true

        VTDNav nav = vg.getNav();
        routeConverter = new RouteConverter(nav);
    }

    @Test(groups = {"ServiceFrame"}, description = "Export Plugin should have one route")
    public void verifyNetwork() throws XPathEvalException, NavException {
        Route route = routeConverter.convert();
        Route routeMock = new Route(); 
        routeMock.setName("METRO");
        Assert.equals(route.getName(), routeMock.getName());
    }

}