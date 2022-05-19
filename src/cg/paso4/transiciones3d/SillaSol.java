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
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class SillaSol extends Applet {
    
    // Parámetros necesarios para crear el escenario para los objetos 3D
    private SimpleUniverse universo = null;
    private Canvas3D lienzo = null;
    
    // Constantes
    private final String FONDO = "src/img/fondos/fondo_sunchair.jpg";
    private final String RUTA_OBJETO = "src\\img\\objetos\\sunchair.obj";
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    
    // Inicializador
    public SillaSol(){
        // Configuración de container
        setLayout(new BorderLayout());
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        lienzo = new Canvas3D(gc);
        add("Center", lienzo);
        universo = new SimpleUniverse(lienzo);
        BranchGroup escena = crearEscenarioGrafico(); 
        universo.addBranchGraph(escena);
    }
    
    private BranchGroup crearEscenarioGrafico(){
        BranchGroup objetoRaiz = new BranchGroup();
        BoundingSphere fronteras = new BoundingSphere(new Point3d(0,0,0),200);
        TextureLoader archivo = new TextureLoader(FONDO, this);
        Background fondo = new Background(archivo.getImage());
        fondo.setApplicationBounds(fronteras);
        objetoRaiz.addChild(fondo);
        objetoRaiz.addChild(modelarObjeto());
        return objetoRaiz;
    }
    
    private BranchGroup modelarObjeto(){
        // Definir ubicación y tamaño del objeto
        BranchGroup objetoRaiz = new BranchGroup();
        TransformGroup grupoPrincipal = new TransformGroup();
        Transform3D transformacion3d = new Transform3D();
        transformacion3d.setTranslation(new Vector3d(0.0,0.0,-1)); //define posicion del objeto
        transformacion3d.setScale(0.001); //define el tamaño del objeto
        grupoPrincipal.setTransform(transformacion3d);
        TransformGroup grupoProducto = new TransformGroup();
        grupoProducto.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        grupoProducto.addChild(cargarObjeto(RUTA_OBJETO));
        
        definirTransición(grupoProducto);
        
        grupoPrincipal.addChild(grupoProducto);
        objetoRaiz.addChild(grupoPrincipal);
        objetoRaiz.compile();
        
        return objetoRaiz;
    }

        
    // Método para definir la transición del objeto
    private void definirTransición(TransformGroup escenaTG) {
        Alpha rotacionAlpha = new Alpha(-1,Alpha.INCREASING_ENABLE,0,0,4000,0,0,0,0,0);
        
        Transform3D yAxis = new Transform3D();
        RotationInterpolator rotar = new RotationInterpolator(rotacionAlpha,escenaTG,yAxis,0.0f,(float) Math.PI*2.0f);
        BoundingBox limites = new BoundingBox(new Point3d(-1000,-1000,-1000),new Point3d(100,100,100));
        rotar.setSchedulingBounds(limites);
        escenaTG.addChild(rotar);
    }
    
    // Método para cargar el objeto
    private BranchGroup cargarObjeto(String filename){
        BranchGroup objetoRaiz = new BranchGroup();
        TransformGroup grupo = new TransformGroup();
        ObjectFile cargar = new ObjectFile();
        
        Scene s = null;
        File archivo = new java.io.File(filename);
        
        try {
           s = cargar.load(archivo.toURI().toURL());
        } catch(IncorrectFormatException | ParsingErrorException | FileNotFoundException | MalformedURLException e) {
            System.err.println(e);
            System.exit(1);
        }
        grupo.addChild(s.getSceneGroup());
        objetoRaiz.addChild(grupo);
        objetoRaiz.compile();
        
        return objetoRaiz;
    }
        
    public static void main (String[] args){
        SillaSol ejecutar = new SillaSol();
        Frame ventana = new MainFrame(ejecutar, FRAME_WIDTH, FRAME_HEIGHT);
    }
}
