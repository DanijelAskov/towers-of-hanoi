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
    
    static {
        PhongMaterial hammeredMetalMaterial = new PhongMaterial();
        hammeredMetalMaterial.setDiffuseMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/hammeredmetal/diff.jpg"));
        hammeredMetalMaterial.setBumpMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/hammeredmetal/bump.jpg"));
        hammeredMetalMaterial.setSpecularMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/hammeredmetal/spec.jpg"));
        HAMMERED_METAL = new Pattern(hammeredMetalMaterial, "Hammered Metal");
        
        PhongMaterial metalMaterial = new PhongMaterial();
        metalMaterial.setDiffuseMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/galvanizedmetal/diff.jpg"));
        metalMaterial.setBumpMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/galvanizedmetal/bump.jpg"));
        metalMaterial.setSpecularMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/galvanizedmetal/spec.jpg"));
        GALVANIZED_METAL = new Pattern(metalMaterial, "Galvanized Metal");
        
        PhongMaterial stoneMaterial = new PhongMaterial();
        stoneMaterial.setDiffuseMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/stone/diff.jpg"));
        stoneMaterial.setBumpMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/stone/bump.jpg"));
        stoneMaterial.setSpecularMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/stone/spec.jpg"));
        STONE = new Pattern(stoneMaterial, "Stone");
        
        PhongMaterial plasticMaterial = new PhongMaterial();
        plasticMaterial.setDiffuseMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/plastic/diff.jpg"));
        PLASTIC = new Pattern(plasticMaterial, "Plastic");
        
        PhongMaterial woodMaterial = new PhongMaterial();
        woodMaterial.setDiffuseMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/wood/diff.jpg"));
        woodMaterial.setBumpMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/wood/bump.jpg"));
        WOOD = new Pattern(woodMaterial, "Wood");
        
        PhongMaterial concreteMaterial = new PhongMaterial();
        concreteMaterial.setDiffuseMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/bluemarble/diff.jpg"));
        concreteMaterial.setBumpMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/bluemarble/bump.jpg"));
        concreteMaterial.setSpecularMap(new Image("askov/schoolprojects/cg/towersofhanoi/resources/maps/bluemarble/spec.jpg"));
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
