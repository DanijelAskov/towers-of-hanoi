/*
 * Copyright (c) 2019, Danijel Askov
 */

package askov.schoolprojects.cg.towersofhanoi.pattern;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

/**
 *
 * @author Danijel Askov
 */
@SuppressWarnings("SameParameterValue")
public class Pattern {

    public static final Pattern HAMMERED_METAL, GALVANIZED_METAL, STONE, PLASTIC, WOOD, BLUE_MARBLE;
    
    private static final PhongMaterial DEFAULT_SELECTED_MATERIAL = new PhongMaterial(Color.web("0x0093C4"));
    private static final PhongMaterial DEFAULT_SELECTABLE_MATERIAL = new PhongMaterial(Color.GREEN);
    private static final PhongMaterial DEFAULT_UNSELECTABLE_MATERIAL = new PhongMaterial(Color.RED);

    private static final String PATH_PREFIX = "maps/";
    
    static {
        PhongMaterial hammeredMetalMaterial = new PhongMaterial();
        hammeredMetalMaterial.setDiffuseMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "hammeredmetal/diff.jpg").toString()));
        hammeredMetalMaterial.setBumpMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "hammeredmetal/bump.jpg").toString()));
        hammeredMetalMaterial.setSpecularMap(new Image(PATH_PREFIX + "hammeredmetal/spec.jpg"));
        HAMMERED_METAL = new Pattern(hammeredMetalMaterial, "Hammered Metal");
        
        PhongMaterial metalMaterial = new PhongMaterial();
        metalMaterial.setDiffuseMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "galvanizedmetal/diff.jpg").toString()));
        metalMaterial.setBumpMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "galvanizedmetal/bump.jpg").toString()));
        metalMaterial.setSpecularMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "galvanizedmetal/spec.jpg").toString()));
        GALVANIZED_METAL = new Pattern(metalMaterial, "Galvanized Metal");
        
        PhongMaterial stoneMaterial = new PhongMaterial();
        stoneMaterial.setDiffuseMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "stone/diff.jpg").toString()));
        stoneMaterial.setBumpMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "stone/bump.jpg").toString()));
        stoneMaterial.setSpecularMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "stone/spec.jpg").toString()));
        STONE = new Pattern(stoneMaterial, "Stone");
        
        PhongMaterial plasticMaterial = new PhongMaterial();
        plasticMaterial.setDiffuseMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "plastic/diff.jpg").toString()));
        PLASTIC = new Pattern(plasticMaterial, "Plastic");
        
        PhongMaterial woodMaterial = new PhongMaterial();
        woodMaterial.setDiffuseMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "wood/diff.jpg").toString()));
        woodMaterial.setBumpMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "wood/bump.jpg").toString()));
        WOOD = new Pattern(woodMaterial, "Wood");
        
        PhongMaterial concreteMaterial = new PhongMaterial();
        concreteMaterial.setDiffuseMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "bluemarble/diff.jpg").toString()));
        concreteMaterial.setBumpMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "bluemarble/bump.jpg").toString()));
        concreteMaterial.setSpecularMap(new Image(Thread.currentThread().getContextClassLoader().getResource(PATH_PREFIX + "bluemarble/spec.jpg").toString()));
        BLUE_MARBLE = new Pattern(concreteMaterial, "Blue Marble");
    }
    
    private final PhongMaterial material;
    private final PhongMaterial selectedMaterial;
    private final PhongMaterial selectableMaterial;
    private final PhongMaterial unselectableMaterial;
    private final String name;
    
    private Pattern(PhongMaterial material, PhongMaterial selectedMaterial, PhongMaterial selectableMaterial, PhongMaterial unselectableMaterial, String name) {
        this.material = material;
        this.selectedMaterial = selectedMaterial;
        this.selectableMaterial = selectableMaterial;
        this.unselectableMaterial = unselectableMaterial;
        this.name = name;
    }
    
    private Pattern(PhongMaterial material, String name) {
        this(material, DEFAULT_SELECTED_MATERIAL, DEFAULT_SELECTABLE_MATERIAL, DEFAULT_UNSELECTABLE_MATERIAL, name);
    }
        
    public void setSpecularPower(double specularPower) {
        material.setSpecularPower(specularPower);
        selectedMaterial.setSpecularPower(specularPower);
    }
    
    public void setSpecularColor(Color color) {
        material.setSpecularColor(color);
        selectedMaterial.setSpecularColor(color);
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public Material getSelectedMaterial() {
        return selectedMaterial;
    }
    
    public Material getSelectableMaterial() {
        return selectableMaterial;
    }
    
    public Material getUnselectableMaterial() {
        return unselectableMaterial;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
