/*
 * Copyright (C) 2019  Danijel Askov
 *
 * This file is part of Towers of Hanoi.
 *
 * Towers of Hanoi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Towers of Hanoi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package askov.schoolprojects.cg.towersofhanoi.shapes;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author Danijel Askov
 */
@SuppressWarnings("ALL")
public class CoaxialCylinder extends MeshView {
    
    public static final int DEFAULT_DIVISIONS = 200;
    
    protected final double innerRadius;
    protected final double outerRadius;
    protected final double height;
    
    public CoaxialCylinder(double innerRadius, double outerRadius, float hegiht, int divs) {
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.height = hegiht;
        
        float[] innerTopCirclePoints = new float[3 * divs]; // 3 coords. x divs divisions
        int currentIndex = 0;
        double currentAngle = 0;
        double angleIncrement = 2 * Math.PI / divs;
        for (int i = 0; i < divs; i++, currentAngle += angleIncrement) {
            innerTopCirclePoints[currentIndex++] = (float)(innerRadius * Math.cos(currentAngle));
            innerTopCirclePoints[currentIndex++] = -hegiht / 2;
            innerTopCirclePoints[currentIndex++] = (float)(innerRadius * Math.sin(currentAngle));
        }
        
        float[] innerBottomCirclePoints = new float[3 * divs];
        currentIndex = 0;
        currentAngle = 0;
        for (int i = 0; i < divs; i++, currentAngle += angleIncrement) {
            innerBottomCirclePoints[currentIndex++] = (float)(innerRadius * Math.cos(currentAngle));
            innerBottomCirclePoints[currentIndex++] = hegiht / 2;
            innerBottomCirclePoints[currentIndex++] = (float)(innerRadius * Math.sin(currentAngle));
        }
        
        float[] outerTopCirclePoints = new float[3 * divs];
        currentIndex = 0;
        currentAngle = 0;
        for (int i = 0; i < divs; i++, currentAngle += angleIncrement) {
            outerTopCirclePoints[currentIndex++] = (float)(outerRadius * Math.cos(currentAngle));
            outerTopCirclePoints[currentIndex++] = -hegiht / 2;
            outerTopCirclePoints[currentIndex++] = (float)(outerRadius * Math.sin(currentAngle));
        }
        
        float[] outerBottomCirclePoints = new float[3 * divs];
        currentIndex = 0;
        currentAngle = 0;
        for (int i = 0; i < divs; i++, currentAngle += angleIncrement) {
            outerBottomCirclePoints[currentIndex++] = (float)(outerRadius * Math.cos(currentAngle));
            outerBottomCirclePoints[currentIndex++] = hegiht / 2;
            outerBottomCirclePoints[currentIndex++] = (float)(outerRadius * Math.sin(currentAngle));
        }
        
        float[] points = new float[4 * 3 * divs];
        float[][] pointsMatrix = {innerTopCirclePoints, innerBottomCirclePoints, outerTopCirclePoints, outerBottomCirclePoints};
        
        currentIndex = 0;
        for (float[] matrix : pointsMatrix) {
            for (float v : matrix) {
                points[currentIndex++] = v;
            }
        }
        
        currentIndex = 0;
        int[] top = new int[divs * 2 * 2 * 3 * 2]; // divs divisions x 2 triangles x 2 faces x 3 points x 2 values (point index + texture index)
        for (int i = 0; i < divs; i++) {
            // First triangle
            // Face
            top[currentIndex++] = i;
            top[currentIndex++] = 2 * (divs + 1) + i;
            
            top[currentIndex++] = 2 * divs + i;
            top[currentIndex++] = 2 * (divs + 1) + i + divs;
            
            top[currentIndex++] = i + 1 > divs - 1 ? 0 : i + 1;
            top[currentIndex++] = i + 1 > divs - 1 ? 2 * (divs + 1) : 2 * (divs + 1) + i + 1;
            
            // Back
            top[currentIndex++] = i;
            top[currentIndex++] = 0;

            top[currentIndex++] = i + 1 > divs - 1 ? 0 : i + 1;
            top[currentIndex++] = 0;
            
            top[currentIndex++] = 2 * divs + i;
            top[currentIndex++] = 0;
            
            // Second triangle
            // Face
            top[currentIndex++] = 2 * divs + i;
            top[currentIndex++] = 2 * (divs + 1) + i + divs;
            
            top[currentIndex++] = i + 1 > divs - 1 ?  2 * divs : 2 * divs + i + 1;
            top[currentIndex++] = i + 1 > divs - 1 ?  2 * (divs + 1) + divs : 2 * (divs + 1) + i + divs + 1;
            
            top[currentIndex++] = i + 1 > divs - 1 ? 0 : i + 1;
            top[currentIndex++] = i + 1 > divs - 1 ? 2 * (divs + 1) : 2 * (divs + 1) + i + 1;
            
            // Back
            top[currentIndex++] = 2 * divs + i;
            top[currentIndex++] = 0;
            
            top[currentIndex++] = i + 1 > divs - 1 ? 0 : i + 1;
            top[currentIndex++] = 0;
            
            top[currentIndex++] = i + 1 > divs - 1 ?  2 * divs : 2 * divs + i + 1;
            top[currentIndex++] = 0;
        }
        
        currentIndex = 0;
        int[] bottom = new int[divs * 2 * 2 * 3 * 2];
        for (int i = 0; i < divs; i++) {
            // First triangle
            // Face
            bottom[currentIndex++] = divs + i;
            bottom[currentIndex++] = 2 * (divs + 1) + i;

            bottom[currentIndex++] = i + 1 > divs - 1 ? divs : divs + i + 1;
            bottom[currentIndex++] = i + 1 > divs - 1 ? 2 * (divs + 1) : 2 * (divs + 1) + i + 1;
            
            bottom[currentIndex++] = 3 * divs + i;
            bottom[currentIndex++] = 2 * (divs + 1) + i + divs;
            
            // Back
            bottom[currentIndex++] = divs + i;
            bottom[currentIndex++] = 0;
            
            bottom[currentIndex++] = 3 * divs + i;
            bottom[currentIndex++] = 0;
            
            bottom[currentIndex++] = i + 1 > divs - 1 ? divs : divs + i + 1;
            bottom[currentIndex++] = 0;
            
            // Second triangle
            // Face
            bottom[currentIndex++] = 3 * divs + i;
            bottom[currentIndex++] = 2 * (divs + 1) + i + divs;
            
            bottom[currentIndex++] = i + 1 > divs - 1 ? divs : divs + i + 1;
            bottom[currentIndex++] = i + 1 > divs - 1 ? 2 * (divs + 1) : 2 * (divs + 1) + i + 1;
            
            bottom[currentIndex++] = i + 1 > divs - 1 ?  3 * divs : 3 * divs + i + 1;
            bottom[currentIndex++] = i + 1 > divs - 1 ?  2 * (divs + 1) + divs : 2 * (divs + 1) + i + divs + 1;

            // Back
            bottom[currentIndex++] = 3 * divs + i;
            bottom[currentIndex++] = 0;
            
            bottom[currentIndex++] = i + 1 > divs - 1 ?  3 * divs : 3 * divs + i + 1;
            bottom[currentIndex++] = 0;
            
            bottom[currentIndex++] = i + 1 > divs - 1 ? divs : divs + i + 1;
            bottom[currentIndex++] = 0;
        }
        
        currentIndex = 0;
        int[] inner = new int[divs * 2 * 2 * 3 * 2];
        for (int i = 0; i < divs; i++) {
            // First triangle
            // Face
            inner[currentIndex++] = i;
            inner[currentIndex++] = i;

            inner[currentIndex++] = i + 1 > divs - 1 ? 0 : i + 1;
            inner[currentIndex++] = i + (divs + 1);
            
            inner[currentIndex++] = divs + i;
            inner[currentIndex++] = i + 1;
            
            // Back
            inner[currentIndex++] = i;
            inner[currentIndex++] = 0;
            
            inner[currentIndex++] = divs + i;
            inner[currentIndex++] = 0;
            
            inner[currentIndex++] = i + 1 > divs - 1 ? 0 : i + 1;
            inner[currentIndex++] = 0;
            
            // Second triangle
            // Face
            inner[currentIndex++] = divs + i;
            inner[currentIndex++] = i + (divs + 1);
            
            inner[currentIndex++] = i + 1 > divs - 1 ? 0 : i + 1;
            inner[currentIndex++] = i + (divs + 1) + 1;
            
            inner[currentIndex++] = i + 1 > divs - 1 ?  divs : divs + i + 1;
            inner[currentIndex++] = i + 1;
            
            // Back
            inner[currentIndex++] = divs + i;
            inner[currentIndex++] = 0;
            
            inner[currentIndex++] = i + 1 > divs - 1 ?  divs : divs + i + 1;
            inner[currentIndex++] = 0;
            
            inner[currentIndex++] = i + 1 > divs - 1 ? 0 : i + 1;
            inner[currentIndex++] = 0;
        }
        
        currentIndex = 0;
        int[] outer = new int[divs * 2 * 2 * 3 * 2];
        for (int i = 0; i < divs; i++) {
            // First triangle
            // Face
            outer[currentIndex++] = 2 * divs + i;
            outer[currentIndex++] = i;
            
            outer[currentIndex++] = 3 * divs + i;
            outer[currentIndex++] = i + (divs + 1);
            
            outer[currentIndex++] = i + 1 > divs - 1 ? 2 * divs : 2 * divs + i + 1;
            outer[currentIndex++] = i + 1;
            
            // Back
            outer[currentIndex++] = 2 * divs + i;
            outer[currentIndex++] = 0;

            outer[currentIndex++] = i + 1 > divs - 1 ? 2 * divs : 2 * divs + i + 1;
            outer[currentIndex++] = 0;
            
            outer[currentIndex++] = 3 * divs + i;
            outer[currentIndex++] = 0;
            
            // Second triangle
            // Face
            outer[currentIndex++] = 3 * divs + i;
            outer[currentIndex++] = i + (divs + 1);
            
            outer[currentIndex++] = i + 1 > divs - 1 ?  3 * divs : 3 * divs + i + 1;
            outer[currentIndex++] = i + (divs + 1) + 1;
            
            outer[currentIndex++] = i + 1 > divs - 1 ? 2 * divs : 2 * divs + i + 1;
            outer[currentIndex++] = i + 1;
            
            // Back
            outer[currentIndex++] = 3 * divs + i;
            outer[currentIndex++] = 0;
            
            outer[currentIndex++] = i + 1 > divs - 1 ? 2 * divs : 2 * divs + i + 1;
            outer[currentIndex++] = 0;
            
            outer[currentIndex++] = i + 1 > divs - 1 ?  3 * divs : 3 * divs + i + 1;
            outer[currentIndex++] = 0;
        }
        
        int[][] facesMatrix = {top, bottom, inner, outer};
        int[] faces = new int[facesMatrix.length * divs * 2 * 2 * 3 * 2];
        currentIndex = 0;
        for (int[] matrix : facesMatrix) {
            for (int i : matrix) {
                faces[currentIndex++] = i;
            }
        }
        
        float[] innerOuterTextureCoords = new float[(2 * (divs + 1)) * 2];
        float part = 1f / divs;
        for (int i = 0; i <= 2 * divs; i += 2) {
            innerOuterTextureCoords[i] = innerOuterTextureCoords[i + 2 * (divs + 1)] = 0.5f * i * part;
            innerOuterTextureCoords[i + 1] = 0;
            innerOuterTextureCoords[i + 2 * (divs + 1) + 1] = 1;
        }
        
        float[] topBottomTextureCoords = new float[(2 * divs) * 2];
        currentIndex = 0;
        currentAngle = 0;
        angleIncrement = 2 * Math.PI / divs;
        for (int i = 0; i < divs; i++, currentAngle += angleIncrement) {
            topBottomTextureCoords[currentIndex++] = (float)(0.50 * (innerRadius / outerRadius) * Math.cos(currentAngle) + 0.50);
            topBottomTextureCoords[currentIndex++] = (float)(0.50 * (innerRadius / outerRadius) * (-Math.sin(currentAngle)) + 0.50);
        }
        currentAngle = 0;
        for (int i = 0; i < divs; i++, currentAngle += angleIncrement) {
            topBottomTextureCoords[currentIndex++] = (float)(0.50 * Math.cos(currentAngle) + 0.50);
            topBottomTextureCoords[currentIndex++] = (float)(0.50 * (-Math.sin(currentAngle)) + 0.50);
        }
        
        float[][] textureCoordsMatrix = {innerOuterTextureCoords, topBottomTextureCoords};
        float[] textureCoords = new float[innerOuterTextureCoords.length + topBottomTextureCoords.length];
        currentIndex = 0;
        for (float[] coordsMatrix : textureCoordsMatrix) {
            for (float matrix : coordsMatrix) {
                textureCoords[currentIndex++] = matrix;
            }
        }

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getFaces().addAll(faces);
        mesh.getTexCoords().addAll(textureCoords);
        
        this.setMesh(mesh);
    }
    
    public CoaxialCylinder(double innerRadius, double outerRadius, float height) {
        this(innerRadius, outerRadius, height, DEFAULT_DIVISIONS);
    }
    
    public double getInnerRadius() {
        return innerRadius;
    }
    
    public double getOuterRadius() {
        return outerRadius;
    }
    
    public double getHeight() {
        return height;
    }
    
}
