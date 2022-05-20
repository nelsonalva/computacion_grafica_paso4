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
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import javax.media.j3d.Alpha;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Asador extends Applet {

    // Parámetros necesarios para crear el escenario para los objetos 3D
    private SimpleUniverse universo = null;
    private Canvas3D lienzo = null;

    // Constantes
    private final String FONDO = "src/img/fondos/fondo_asador.jpg";
    private final String RUTA_OBJETO = "src\\img\\objetos\\asador.obj";
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;

    public Asador() {
        // Configuración de container
        setLayout(new BorderLayout());
        GraphicsConfiguration configuracionGrafica = SimpleUniverse.getPreferredConfiguration();
        lienzo = new Canvas3D(configuracionGrafica);
        add("Center", lienzo);
        universo = new SimpleUniverse(lienzo);
        BranchGroup escena = crearEscenarioGrafico();
        universo.addBranchGraph(escena);
    }

    private BranchGroup crearEscenarioGrafico() {
        BranchGroup objetoRaiz = new BranchGroup();
        BoundingSphere fronteras = new BoundingSphere(new Point3d(0, 0, 0), 200);
        TextureLoader archivo = new TextureLoader(FONDO, this);
        Background fondo = new Background(archivo.getImage());
        fondo.setApplicationBounds(fronteras);
        objetoRaiz.addChild(fondo);
        objetoRaiz.addChild(modelarObjeto());
        return objetoRaiz;
    }

        // Definir ubicación y tamaño del objeto
    private BranchGroup modelarObjeto() {
        BranchGroup objetoRaiz = new BranchGroup();
        TransformGroup grupo = new TransformGroup();
        Transform3D transformacion3d = new Transform3D();
        transformacion3d.setTranslation(new Vector3d(0.0, -0.2, -1));
        transformacion3d.setScale(0.0022);
        grupo.setTransform(transformacion3d);
        TransformGroup producto = new TransformGroup();
        producto.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        producto.addChild(cargarObjeto(RUTA_OBJETO));

        definirTransición(producto);
        
        grupo.addChild(producto);
        objetoRaiz.addChild(grupo);
        objetoRaiz.compile();

        return objetoRaiz;
    }

    // Método para definir la transición del objeto
    private void definirTransición(TransformGroup producto) {
        Alpha alpha = new Alpha(-1, 30000);
        Transform3D eje = new Transform3D();
        eje.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
        RotationInterpolator girar = new RotationInterpolator(alpha, producto, eje, 0.0f, (float) Math.PI * (6.0f));
        BoundingSphere fronteras = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
        girar.setSchedulingBounds(fronteras);
        producto.addChild(girar);
    }

    // Método para cargar el objeto
    private BranchGroup cargarObjeto(String nombreArchivo) {
        BranchGroup objetoRaiz = new BranchGroup();
        TransformGroup grupo = new TransformGroup();
        ObjectFile cargar = new ObjectFile();

        Scene s = null;
        File archivo = new java.io.File(nombreArchivo);

        try {
            s = cargar.load(archivo.toURI().toURL());
        } catch (IncorrectFormatException | ParsingErrorException | FileNotFoundException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }
        grupo.addChild(s.getSceneGroup());
        objetoRaiz.addChild(grupo);
        objetoRaiz.compile();

        return objetoRaiz;
    }

    public static void main(String[] args) {
        Asador asador = new Asador();
        Frame ventana = new MainFrame(asador, FRAME_WIDTH, FRAME_HEIGHT);
    }
}
