package cg.paso4.transiciones3d;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.io.FileNotFoundException;
import javax.media.j3d.Alpha;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;

public class SillaSol extends Applet {

    // Parámetros necesarios para crear el escenario para los objetos 3D
    BoundingBox limites = new BoundingBox(new Point3d(-10f, -10f, -10f), new Point3d(10f, 10f, 10f));

    // Constantes
    private final String FONDO = "src/img/fondos/fondo_sunchair.jpg";
    private final String RUTA_OBJETO = "src\\img\\objetos\\sunchair.obj";
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;

    // Inicializador - Configuración de container
    public SillaSol() {
        setLayout(new BorderLayout());
        GraphicsConfiguration configuracionGrafica = SimpleUniverse.getPreferredConfiguration();
        Canvas3D lienzo = new Canvas3D(configuracionGrafica);
        add("Center", lienzo);
        BranchGroup grupoEscena = modelarObjeto();
        SimpleUniverse universe = new SimpleUniverse(lienzo);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(grupoEscena);
    }

    // Definir ubicación y tamaño del objeto
    private BranchGroup modelarObjeto() {
        BranchGroup objetoRaiz = new BranchGroup();
        objetoRaiz.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objetoRaiz.addChild(crearFondo());//se carga la imagen de fondo correspondiente
        TransformGroup grupoPrincipal = new TransformGroup();
        grupoPrincipal.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objetoRaiz.addChild(grupoPrincipal);

        definirTransicion(grupoPrincipal);

        return objetoRaiz;
    }
    
    private TransformGroup crearFondo() {
        TransformGroup fondo = new TransformGroup();
        TextureLoader tlfondo = new TextureLoader(FONDO, this);
//        fondo.addChild(util.aplicarFondo(tlfondo, limites));//aplica el fondo
        Background background = new Background();
        background.setImage(tlfondo.getImage());
        background.setImageScaleMode(Background.SCALE_FIT_ALL);
        background.setApplicationBounds(limites);
        fondo.addChild(background);

        return fondo;
    }

    // Método para definir la transición del objeto
    private void definirTransicion(TransformGroup grupoPrincipal) {
        Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 10000, 0, 0, 0, 0, 0);
        Transform3D transformacion3D = new Transform3D();
        PositionInterpolator desplazar = new PositionInterpolator(alpha, grupoPrincipal, transformacion3D, -1f, 1f);
        desplazar.setSchedulingBounds(limites);
        grupoPrincipal.addChild(desplazar);
        TransformGroup grupoProducto = new TransformGroup();
        Transform3D transform3DEscala = new Transform3D();
        transform3DEscala.setScale(0.3);
        grupoProducto.setTransform(transform3DEscala);
//        grupoProducto.setTransform(util.crearScale(0.4));
        grupoPrincipal.addChild(grupoProducto);
        Scene escena = cargarObjeto(RUTA_OBJETO);//se carga el modelo 3d obj
        grupoProducto.addChild(escena.getSceneGroup());
    }



    // Método para cargar el objeto
    private Scene cargarObjeto(String ruta) {
        ObjectFile file = new ObjectFile();
        file.setFlags(ObjectFile.RESIZE);
        Scene escena = null;
        try {
//            escena = file.load(nombreArchivo == null ? ruta : nombreArchivo);
            escena = file.load(ruta);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (ParsingErrorException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        return escena;
    }

    public static void main(String[] args) {
        SillaSol sillaSol = new SillaSol();
        Frame frame = new MainFrame(sillaSol, FRAME_WIDTH, FRAME_HEIGHT);
    }
}
