package view;

/*
 * Developed by Arthur Arantes Faria 
 * Graduating in Computer Science on UNIFOR-MG BRASIL
 * arthurarantes23@hotmail.com
 */

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import geometry.Pyramid;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author Arthur
 */
public class R2d2 extends JFrame implements KeyListener{

    //Criação de atributos necessários para 
    private VirtualUniverse universe;
    private Locale locale;
    private Canvas3D canvas3D;
    private boolean screenMode = true;
    BranchGroup r2d2;
    Transform3D rotation, rotBody, rotHead;
    TransformGroup tgAxis, tgBody, tgHead, tgBBase, tgFootL, tgFootR, tgBoom;
    TransformGroup tgArmJointR, tgArmJointL, tgArmSupL, tgArmSupR, tgArmInfL, tgArmInfR;

    Transform3D t3dAxis, t3dBody, t3dHead, t3dBBase, t3dFootL, t3dFootR, t3dBoom;
    Transform3D t3dArmJointR, t3dArmJointL, t3dArmSupR, t3dArmSupL, t3dArmInfL, t3dArmInfR;
    Double scale;
    Pyramid pyramid;
    

    /**
     * Constructor
     */
    public R2d2() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(dimension.width, dimension.height);
        if (screenMode) {
            this.setUndecorated(true);
        } else {
            this.setTitle("R2D2");
        }
        scale = 0.1;
        addKeyListener(this);

        universe = new VirtualUniverse();
        locale = new Locale(universe);
        GraphicsConfigTemplate3D g3d = new GraphicsConfigTemplate3D();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getBestConfiguration(g3d);
        canvas3D = new Canvas3D(gc);
        canvas3D.setSize(dimension.width, dimension.height);
        canvas3D.addKeyListener(this);
        pyramid = new Pyramid();

        r2d2 = new BranchGroup();
        tgAxis = new TransformGroup();
        tgBody = new TransformGroup();
        tgHead = new TransformGroup();
        tgBBase = new TransformGroup();
        tgArmJointL = new TransformGroup();
        tgArmJointR = new TransformGroup();
        tgArmSupL = new TransformGroup();
        tgArmSupR = new TransformGroup();
        tgArmInfL = new TransformGroup();
        tgArmInfR = new TransformGroup();
        tgFootL = new TransformGroup();
        tgFootR = new TransformGroup();
        tgBoom = new TransformGroup();

        t3dBoom = new Transform3D();
        t3dAxis = new Transform3D();
        t3dBody = new Transform3D();
        t3dHead = new Transform3D();
        t3dBBase = new Transform3D();
        t3dArmJointL = new Transform3D();
        t3dArmJointR = new Transform3D();
        t3dArmSupL = new Transform3D();
        t3dArmSupR = new Transform3D();
        t3dArmInfL = new Transform3D();
        t3dArmInfR = new Transform3D();
        t3dFootL = new Transform3D();
        t3dFootR = new Transform3D();

        // Setando Permissões
        tgAxis.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBody.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgHead.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBoom.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotation = new Transform3D();
        rotBody = new Transform3D();
        rotHead = new Transform3D();
        
        //Adicionando os grafos de contexto e de visualização no Locale
        locale.addBranchGraph(createViewGraph());
        r2d2 = createContextGraph();
        r2d2.setCapability(BranchGroup.ALLOW_DETACH);
        locale.addBranchGraph(r2d2);

        scale = 0.1;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.getContentPane().add(canvas3D);
        this.show();        
        // TODO: Terminar tela e enviar para a tela anterior
    }

    //Grafo de Visualização
    BranchGroup createViewGraph() {
        BranchGroup viewPlatformBG = new BranchGroup();
        View view = new View();
        ViewPlatform viewPlatform = new ViewPlatform();
        PhysicalBody physicalBody = new PhysicalBody(new Point3d(0, 0, 0), new Point3d(0, 0, 0));
        PhysicalEnvironment physicalEnvironment = new PhysicalEnvironment();
        view.attachViewPlatform(viewPlatform);
        view.setPhysicalBody(physicalBody);
        view.setPhysicalEnvironment(physicalEnvironment);
        view.addCanvas3D(canvas3D);
        Transform3D transform3D = new Transform3D();
        transform3D.set(new Vector3f(0.0f, 0.0f, 20.0f));
        TransformGroup viewPlatformTG = new TransformGroup(transform3D);
        viewPlatformTG.addChild(viewPlatform);
        viewPlatformBG.addChild(viewPlatformTG);
        return viewPlatformBG;
    }

    //Função que cria uma textura a patir de uma uma imagem e retorna um objeto Appearance
    Appearance createImageAppearance(String dir) {
        TextureLoader loader = new TextureLoader("src/images/" + dir,
                "RGB", new Container());
        Texture texture = loader.getTexture();
        Appearance ap = new Appearance();
        ap.setTexture(texture);
        return ap;
    }

    //Grafo de Contexto
    BranchGroup createContextGraph() {
        BranchGroup contextGraph = new BranchGroup();
        //Atributos para a definição de aparencia
        Appearance appBody = new Appearance();
        //Objetos para a configuração de cores
        ColoringAttributes colorBody = new ColoringAttributes();
        colorBody.setColor(new Color3f(1.0f, 1.0f, 1.0f));
        //Criação de codernadas para o posicionamento de texturas em objetos
        int primflags = Primitive.GENERATE_NORMALS
                + Primitive.GENERATE_TEXTURE_COORDS;
        //Definindo uma cor na aparencia
        appBody.setColoringAttributes(colorBody);
        //Atributo para auxiliar na translação e rotação de objetos
        Transform3D transform3D = new Transform3D();

        Sphere sphereBoom = new Sphere(0.1f, primflags, createImageAppearance("boom.jpg"));
        tgBoom.addChild(sphereBoom);

        //Criação de um ciindro que representa a base do corpo do robo
        Cylinder cylinderBBase = new Cylinder(0.9f, 0.2f, primflags, createImageAppearance("base.jpg"));
        //Adiciona o objeto cylinderBBase como filho do transformGroup
        tgBBase.addChild(cylinderBBase);
        //Ajusta a posição do objeto em relação ao corpo do robo
        t3dBBase.set(new Vector3f(0.0f, -1.1f, 0.0f));
        tgBBase.setTransform(t3dBBase);

        //Geomeria Livre em forma de Piramide para a formação do Pé Esquerdo do robo
        tgFootL = pyramid.createPyramid(appBody);
        //Ajusta a posição do pé esquerdo em relação ao braço esquerdo
        t3dFootL.set(new Vector3f(0.0f, 0.0f, -1.0f));
        //Seta a transformação
        tgFootL.setTransform(t3dFootL);
        transform3D = new Transform3D();
        //Ajusta a rotação do pé esquerdo em relação ao braço esquerdo
        transform3D.rotX(Math.PI / 2);
        //Multiplica a matriz em relação a posição atual do pé esquerdo
        t3dFootL.mul(transform3D);
        //Seta transformação do pé esquerdo
        tgFootL.setTransform(t3dFootL);
        //Ajusta novamente a rotação do pé
        transform3D.rotY(-Math.PI / 4);
        //Multiplica a matriz de rotação
        t3dFootL.mul(transform3D);
        //Seta as configurações de rotação
        tgFootL.setTransform(t3dFootL);

        //Geomeria Livre em forma de Piramide para a formação do Pé Direito do robo
        tgFootR = pyramid.createPyramid(appBody);
        //Ajusta a posição do pé esquerdo em relação ao braço esquerdo
        t3dFootR.set(new Vector3f(0.0f, 0.0f, -1.0f));
        //Seta a transformação
        tgFootR.setTransform(t3dFootR);
        transform3D = new Transform3D();
        //Ajusta a rotação do pé direito em relação ao braço direito
        transform3D.rotX(Math.PI / 2);
        //Multiplica a matriz em relação a posição atual do pé esquerdo
        t3dFootR.mul(transform3D);
        //Seta transformação do pé esquerdo
        tgFootR.setTransform(t3dFootR);
        //Ajusta novamente a rotação do pé
        transform3D.rotY(-Math.PI / 4);
        //Multiplica a matriz de rotação
        t3dFootR.mul(transform3D);
        //Seta as configurações de rotação
        tgFootR.setTransform(t3dFootR);

        //Cria um cilindro com uma textura que representa a junta que liga o braço Esquerdo ao eixo do robo. 
        Cylinder cylinderArmJointL = new Cylinder(0.35f, 0.3f, primflags, createImageAppearance("armjoint.jpg"));
        //Adiciona o objeto ao TransformGroup
        tgArmJointL.addChild(cylinderArmJointL);
        //Adiciona tgArmSupL como filho
        tgArmJointL.addChild(tgArmSupL);
        //Ajusta a posição do objeto
        t3dArmJointL.set(new Vector3f(0.0f, -1.2f, 0.0f));
        tgArmJointL.setTransform(t3dArmJointL);

        //Cria um cilindro com uma textura que representa a junta que liga o braço Direito ao eixo do robo 
        Cylinder cylinderArmJointR = new Cylinder(0.35f, 0.3f, primflags, createImageAppearance("armjointR.jpg"));
        //Adiciona o objeto ao TransformGroup
        tgArmJointR.addChild(cylinderArmJointR);
        //Adiciona tgArmSupR como filho
        tgArmJointR.addChild(tgArmSupR);
        //Ajusta a posição do objeto
        t3dArmJointR.set(new Vector3f(0.0f, 1.2f, 0.0f));
        tgArmJointR.setTransform(t3dArmJointR);

        //Cria uma caixa com uma textura que representa a parte superior do braço Esquerdo do robo
        Box boxArmSupL = new Box(0.35f, 0.15f, 0.35f, primflags, createImageAppearance("armSup.jpg"));
        //Adiciona o objeto ao TransformGroup
        tgArmSupL.addChild(boxArmSupL);
        tgArmSupL.addChild(tgArmInfL);
        //Ajusta a posição do objeto
        t3dArmSupL.set(new Vector3f(0.0f, 0.0f, -0.31f));
        tgArmSupL.setTransform(t3dArmSupL);

        //Cria uma caixa com uma textura que representa a parte superior do braço Direito do robo
        Box boxArmSupR = new Box(0.35f, 0.15f, 0.35f, primflags, createImageAppearance("armSupR.jpg"));
        //Adiciona o objeto ao TransformGroup
        tgArmSupR.addChild(boxArmSupR);
        //Adciona tgArmInfR como filho;
        tgArmSupR.addChild(tgArmInfR);
        //Ajusta a posição do objeto
        t3dArmSupR.set(new Vector3f(0.0f, 0.0f, -0.31f));
        tgArmSupR.setTransform(t3dArmSupR);

        //Cria auma caixa com textura que representa o braço Esquerdo do robo
        Box boxArmInfL = new Box(0.15f, 0.12f, 0.7f, primflags, createImageAppearance("arm.jpg"));
        //Adiciona o objeto ao TransformGroup
        tgArmInfL.addChild(boxArmInfL);
        //Adciona tgFooL como filho
        tgArmInfL.addChild(tgFootL);
        //Ajusta a posição do objeto
        t3dArmInfL.set(new Vector3f(0.0f, 0.0f, -1.05f));
        tgArmInfL.setTransform(t3dArmInfL);

        //Cria auma caixa com textura que representa o braço Direito do robo
        Box boxArmInfR = new Box(0.15f, 0.12f, 0.7f, primflags, createImageAppearance("armR.jpg"));
        //Adiciona o objeto ao TransformGroup
        tgArmInfR.addChild(boxArmInfR);
        //Adciona tgFoolR como filho
        tgArmInfR.addChild(tgFootR);
        //Ajusta a posição do objeto
        t3dArmInfR.set(new Vector3f(0.0f, 0.0f, -1.05f));
        tgArmInfR.setTransform(t3dArmInfR);

        //Cria uma esfera com textura que representa a cabeça do robo
        Sphere sphereHead = new Sphere(1.0f, primflags, createImageAppearance("head.jpg"));
        //Adiciona o objeto ao TransformGroup
        tgHead.addChild(sphereHead);
        //Ajusta a posição do objeto
        t3dHead.set(new Vector3f(0.0f, 1.1f, 0.0f));
        tgHead.setTransform(t3dHead);

        //Cria um cilindro com textura que representa o corpo do robo
        Cylinder cylinderBody = new Cylinder(1.0f, 2.0f, primflags, createImageAppearance("body.jpg"));
        //Adiciona o objeto ao TransformGroup
        tgBody.addChild(cylinderBody);
        tgBody.addChild(tgHead);
        tgBody.addChild(tgBBase);
        //Ajusta a posição do objeto
        t3dBody.set(new Vector3f(0.0f, 0.0f, -0.6f));
        transform3D = new Transform3D();
        //Ajusta a rotação do Objeto
        transform3D.rotZ(-Math.PI / 2);
        t3dBody.mul(transform3D);
        tgBody.setTransform(t3dBody);
        transform3D.rotX(Math.PI / 2);
        t3dBody.mul(transform3D);
        tgBody.setTransform(t3dBody);

        //Criação de um eixo central com forma de cilindro que apoia o corpo e liga os dois baços do robo
        Cylinder cylinderAxis = new Cylinder(0.175f, 2.10f, appBody);
        //Adiciona o objeto ao TransformGroup
        tgAxis.addChild(cylinderAxis);
        //Adiciona tgBody, tgArmJointL, tgArmJointR e tgBoom como filhos
        tgAxis.addChild(tgBody);
        tgAxis.addChild(tgArmJointL);
        tgAxis.addChild(tgArmJointR);
        tgAxis.addChild(tgBoom);
        //Ajusta a rotação do Objeto
        transform3D.rotZ(Math.PI / 2);
        rotation.mul(transform3D);
        tgAxis.setTransform(rotation);
        transform3D.rotY(Math.PI / 2);
        rotation.mul(transform3D);
        tgAxis.setTransform(rotation);
        //Adiciona o TransformGroup  ao 
        contextGraph.addChild(tgAxis);
        return contextGraph;

    }

    //Evento de botão
    @Override
    public void keyPressed(KeyEvent e) {
        int pos = 100;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        int kP = e.getKeyCode();
        Transform3D temp = new Transform3D();
        switch (kP) {
            case (KeyEvent.VK_UP):
                //Rotaciona o corpo do Botão para frente
                tgBody.getTransform(t3dBody);
                temp.rotY(-Math.PI / pos);
                temp.mul(t3dBody);
                tgBody.setTransform(temp);
                break;
            case (KeyEvent.VK_DOWN):
                //Rotaciona o corpo do Botão para trás
                tgBody.getTransform(t3dBody);
                temp.rotY(Math.PI / pos);
                temp.mul(t3dBody);
                tgBody.setTransform(temp);
                break;
            case (KeyEvent.VK_RIGHT):
                //Gira o robo para direita
                temp.rotZ(-Math.PI / pos);
                rotation.mul(temp);
                tgAxis.setTransform(rotation);
                break;

            case (KeyEvent.VK_LEFT):
                //Gira o robo para esquerda
                temp.rotZ(Math.PI / pos);
                rotation.mul(temp);
                tgAxis.setTransform(rotation);
                break;
            case (KeyEvent.VK_Z):
                //Gira a cabeça do robo para esquerda
                temp.rotY(-Math.PI / pos);
                t3dHead.mul(temp);
                tgHead.setTransform(t3dHead);
                break;
            case (KeyEvent.VK_X):
                //Gira a cabeça do robo para direita
                temp.rotY(Math.PI / pos);
                t3dHead.mul(temp);
                tgHead.setTransform(t3dHead);
                break;
            case (KeyEvent.VK_A):
                //Move o robo para a esquerda
                temp.setTranslation(new Vector3f(-0.10f, 0.f, 0.0f));
                rotation.mul(temp);
                tgAxis.setTransform(rotation);
                temp.rotZ(Math.PI / pos);
                rotation.mul(temp);
                tgAxis.setTransform(rotation);
                break;
            case (KeyEvent.VK_D):
                //Move o robo para a direita
                temp.setTranslation(new Vector3f(-0.10f, 0.f, 0.0f));
                rotation.mul(temp);
                tgAxis.setTransform(rotation);
                temp.rotZ(-Math.PI / pos);
                rotation.mul(temp);
                tgAxis.setTransform(rotation);
                break;
            case (KeyEvent.VK_W):
                //Move o robo para frente
                temp.setTranslation(new Vector3f(-0.10f, 0.f, 0.0f));
                rotation.mul(temp);
                tgAxis.setTransform(rotation);
                break;
            case (KeyEvent.VK_S):
                //Move o robo para tras
                temp.setTranslation(new Vector3f(0.1f, 0.f, 0.0f));
                rotation.mul(temp);
                tgAxis.setTransform(rotation);
                break;
            case (KeyEvent.VK_SPACE):
                //"Explode" o robo
                if (r2d2.isLive()) {
                    for (int i = 1; i <= 650; i++) {

                        scale += 0.1;
                        t3dBoom.setScale(scale);
                        tgBoom.setTransform(t3dBoom);
                        temp.rotZ(Math.PI / pos / 2);
                        t3dBoom.mul(temp);
                        tgBoom.setTransform(t3dBoom);

                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException ex) {
                            
                        }

                    }
                    scale = 0.0;
                    t3dBoom.setScale(scale);
                    tgBoom.setTransform(t3dBoom);
                    locale.removeBranchGraph(r2d2);
                } else {
                    locale.addBranchGraph(r2d2);
                }
                break;
            case KeyEvent.VK_H:
                // help
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
