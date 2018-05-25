/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Animation;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 *
 * @author Bolska
 */
public class TranslateResizeTransition extends Transition{
    
    protected Region region;
    protected double startWidth;
    protected double sumWidth;
    protected TranslateTransition translate;
    
    
    public void setTranslateResizeTransition(Region region, double fromX, double toX, double duration, double sumWidth) {
        setCycleDuration(Duration.millis(duration));
        this.region = region;
        this.sumWidth = sumWidth;
        this.startWidth = region.getWidth();
        translate = new TranslateTransition();
        translate.setFromX(fromX);
        translate.setToX(toX);
    }

    @Override
    protected void interpolate(double fraction) {
        region.setTranslateX(translate.getFromX() + (translate.getToX() - translate.getFromX()) * fraction);
        region.setPrefWidth(startWidth + (sumWidth * fraction));
    }
    
}
