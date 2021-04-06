package boardcad.export;

import java.io.File;

import java.io.PrintStream;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import board.BezierBoard;

//An ASCII STL file begins with the line
//
//solid name
//where name is an optional string (though if name is omitted there must still be a space after solid). The file continues with any number of triangles, each represented as follows:
//
//facet normal ni nj nk
//    outer loop
//        vertex v1x v1y v1z
//        vertex v2x v2y v2z
//        vertex v3x v3y v3z
//    endloop
//endfacet
//where each n or v is a floating-point number in sign-mantissa-"e"-sign-exponent format, e.g., "2.648000e-002". The file concludes with
//
//endsolid name

public class StlExport {
	static public void exportBezierBoard(String filename, BezierBoard brd, int deckSegments, int bottomSegments, int lengthSegments)
	{
		try {
			File file = new File(filename);
			PrintStream p = new PrintStream(file);

			writeStartOfFile(p);

			double xPos = 0.0;
			double length = brd.getLength();
			double lengthStep = length / lengthSegments;

			// Deck
			double minAngle = -10.0;
			double maxAngle = 150.0;

			Point3d[][] deckVertices = new Point3d[deckSegments+1][lengthSegments+1];
			Vector3f[][] deckNormals = new Vector3f[deckSegments+1][lengthSegments+1];

			//Generate deck coordinates
			for (int i = 0; i <= deckSegments; i++) {
				xPos = 0.0;
				for (int j = 0; j <= lengthSegments; j++) {
					deckVertices[i][j] = new Point3d(brd.getSurfacePoint(xPos, minAngle, maxAngle, i, deckSegments));
					deckNormals[i][j] = new Vector3f(brd.getSurfaceNormal(xPos, minAngle, maxAngle, i, deckSegments));

					xPos += lengthStep;
				}
			}

			//Generate triangles
			for (int i = 0; i < deckSegments; i++) {
				for (int j = 0; j < lengthSegments; j++) {
					Vector3f n1 = deckNormals[i][j];
					Vector3f n2 = deckNormals[i][j+1];
					Vector3f n3 = deckNormals[i+1][j+1];
					Vector3f n = new Vector3f(n1);
					n.add(n2);
					n.add(n3);
					n.normalize();

					Point3d p1 = new Point3d(deckVertices[i][j]);
					Point3d p2 = new Point3d(deckVertices[i][j+1]);
					Point3d p3 = new Point3d(deckVertices[i+1][j+1]);
					Point3d p4 = new Point3d(deckVertices[i+1][j]);
					if(i == 0){	// Close seam at stringer by setting y pos to zero
						p1.setY(0.0);
						p2.setY(0.0);
					}
					writeTriangle(p, p1, p2, p3, n);
					writeTriangle(p, p1, p3, p4, n);

					//Mirror
					p1.setY(-p1.y);
					p2.setY(-p2.y);
					p3.setY(-p3.y);
					p4.setY(-p4.y);
					n.setY(-n.y);
					writeTriangle(p, p1, p3, p2, n);
					writeTriangle(p, p1, p4, p3, n);
				}
			}

			Point3d[][] bottomVertices = new Point3d[deckSegments+1][lengthSegments+1];
			Vector3f[][] bottomNormals = new Vector3f[deckSegments+1][lengthSegments+1];

			//Generate bottom
			minAngle = maxAngle;
			maxAngle = 370.0;
			for (int i = 0; i <= bottomSegments; i++) {
				xPos = 0;
				for (int j = 0; j <= lengthSegments; j++) {
					bottomVertices[i][j] = brd.getSurfacePoint(xPos, minAngle, maxAngle, i, bottomSegments);
					bottomNormals[i][j] = new Vector3f(brd.getSurfaceNormal(xPos, minAngle, maxAngle,i, bottomSegments));
					xPos += lengthStep;
				}
			}

			//Generate triangles
			for (int i = 0; i < bottomSegments; i++) {
				for (int j = 0; j < lengthSegments; j++) {
					Vector3f n1 = bottomNormals[i][j];
					Vector3f n2 = bottomNormals[i][j+1];
					Vector3f n3 = bottomNormals[i+1][j+1];
					Vector3f n = new Vector3f(n1);
					n.add(n2);
					n.add(n3);
					n.normalize();

					Point3d p1 = new Point3d(bottomVertices[i][j]);
					Point3d p2 = new Point3d(bottomVertices[i][j+1]);
					Point3d p3 = new Point3d(bottomVertices[i+1][j+1]);
					Point3d p4 = new Point3d(bottomVertices[i+1][j]);
					if(i == bottomSegments - 1){	// Close seam at stringer by setting y pos to zero
						p3.setY(0.0);
						p4.setY(0.0);
					}
					writeTriangle(p, p1, p2, p3, n);
					writeTriangle(p, p1, p3, p4, n);

					//Mirror
					p1.setY(-p1.y);
					p2.setY(-p2.y);
					p3.setY(-p3.y);
					p4.setY(-p4.y);
					n.setY(-n.y);
					writeTriangle(p, p1, p3, p2, n);
					writeTriangle(p, p1, p4, p3, n);
				}
			}

			//Generate triangles for end of tail
			Vector3f n = new Vector3f(-1.0f, 0.0f, 0.0f);
			Point3d p1 = new Point3d(bottomVertices[0][0]);
			p1.add(deckVertices[0][0]);
			p1.scale(.5);
			p1.setY(0.0);
			for (int i = 0; i < bottomSegments; i++) {
				Point3d p2 = new Point3d(bottomVertices[i][0]);
				Point3d p3 = new Point3d(bottomVertices[i+1][0]);
				if(i == bottomSegments - 1){	// Close seam at stringer by setting y pos to zero
					p3.setY(0.0);
				}
				writeTriangle(p, p1, p2, p3, n);

				//Mirror
				p2.setY(-p2.y);
				p3.setY(-p3.y);
				writeTriangle(p, p1, p3, p2, n);
			}
			for (int i = 0; i < deckSegments; i++) {
				Point3d p2 = new Point3d(deckVertices[i][0]);
				Point3d p3 = new Point3d(deckVertices[i+1][0]);
				if(i == 0){	// Close seam at stringer by setting y pos to zero
					p2.setY(0.0);
				}
				writeTriangle(p, p1, p2, p3, n);

				//Mirror
				p2.setY(-p2.y);
				p3.setY(-p3.y);
				writeTriangle(p, p1, p3, p2, n);
			}

			//Generate triangles for end of nose
			n.setX(1.0f);
			p1 = new Point3d(bottomVertices[0][lengthSegments-1]);
			p1.add(deckVertices[0][lengthSegments-1]);
			p1.scale(.5);
			p1.setY(0.0);
			for (int i = 0; i < bottomSegments; i++) {
				Point3d p2 = new Point3d(bottomVertices[i][lengthSegments-1]);
				Point3d p3 = new Point3d(bottomVertices[i+1][lengthSegments-1]);
				if(i == bottomSegments - 1){	// Close seam at stringer by setting y pos to zero
					p3.setY(0.0);
				}
				writeTriangle(p, p1, p3, p2, n);

				//Mirror
				p2.setY(-p2.y);
				p3.setY(-p3.y);
				writeTriangle(p, p1, p2, p3, n);
			}
			for (int i = 0; i < deckSegments; i++) {
				Point3d p2 = new Point3d(deckVertices[i][lengthSegments-1]);
				Point3d p3 = new Point3d(deckVertices[i+1][lengthSegments-1]);
				if(i == 0){	// Close seam at stringer by setting y pos to zero
					p2.setY(0.0);
				}
				writeTriangle(p, p1, p3, p2, n);

				//Mirror
				p2.setY(-p2.y);
				p3.setY(-p3.y);
				writeTriangle(p, p1, p2, p3, n);
			}

			writeEndOfFile(p);

			p.close();	// Also flushes
		} catch (Exception e) {
			System.out.printf("Failed to write STL File, %s", e.toString());
			e.printStackTrace();
		}
	}

	static void writeStartOfFile(PrintStream p){
		p.println("solid brd");
	}

	static void writeEndOfFile(PrintStream p){
		p.println("endsolid brd");
	}

	static void writeTriangle(PrintStream p, Point3d p1, Point3d p2, Point3d p3, Vector3f n){
		p.printf("facet normal %f %f %f\nouter loop\nvertex %f %f %f\nvertex %f %f %f\nvertex %f %f %f\nendloop\nendfacet\n", n.x, n.y, n.z, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z);
	}
}
