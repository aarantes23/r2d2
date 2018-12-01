package geometry;

/*
 * Developed by Arthur Arantes Faria 
 * Graduating in Computer Science on UNIFOR-MG BRASIL
 * arthurarantes23@hotmail.com
 */

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 *
 * @author Arthur
 */
public class Pyramid {

    // Used to create Left Foot from r2d2    
    private TransformGroup group;

    public Pyramid() {
        group = new TransformGroup();
    }

    public TransformGroup createPyramid(Appearance appearance) {
        group = new TransformGroup();
        //Determia a posição dos vertices
        Point3f e = new Point3f(0.7f, 0.0f, -0.1f); // east
        Point3f s = new Point3f(-0.1f, 0.0f, 0.7f); // south
        Point3f w = new Point3f(-0.7f, 0.0f, 0.1f); // west
        Point3f n = new Point3f(0.1f, 0.0f, -0.7f); // north
        Point3f t = new Point3f(0.0f, 0.85f, 0.0f); // top

        TriangleArray pyramidGeometry = new TriangleArray(18,
                TriangleArray.COORDINATES);

        //Setando as cordenadas
        pyramidGeometry.setCoordinate(0, e);
        pyramidGeometry.setCoordinate(1, t);
        pyramidGeometry.setCoordinate(2, s);

        pyramidGeometry.setCoordinate(3, s);
        pyramidGeometry.setCoordinate(4, t);
        pyramidGeometry.setCoordinate(5, w);

        pyramidGeometry.setCoordinate(6, w);
        pyramidGeometry.setCoordinate(7, t);
        pyramidGeometry.setCoordinate(8, n);

        pyramidGeometry.setCoordinate(9, n);
        pyramidGeometry.setCoordinate(10, t);
        pyramidGeometry.setCoordinate(11, e);

        pyramidGeometry.setCoordinate(12, e);
        pyramidGeometry.setCoordinate(13, s);
        pyramidGeometry.setCoordinate(14, w);

        pyramidGeometry.setCoordinate(15, w);
        pyramidGeometry.setCoordinate(16, n);
        pyramidGeometry.setCoordinate(17, e);
        GeometryInfo geometryInfo = new GeometryInfo(pyramidGeometry);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(geometryInfo);

        GeometryArray result = geometryInfo.getGeometryArray();
        Shape3D shape = new Shape3D(result, appearance);

        group.addChild(shape);

        return group;
    }

}
