/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Animation;

import com.jfoenix.controls.JFXHamburger;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 *
 * @author Bolska
 */
public class TransitionAnimation {
    
    private TranslateTransition translate;
    private TranslateResizeTransition transition;
    private RotateTransition rotate;
    private ScaleTransition scale;
    
    public TransitionAnimation(){
        translate = new TranslateTransition();
        transition = new TranslateResizeTransition();
        rotate = new RotateTransition();
        scale = new ScaleTransition();
    }
    
    public void setTranslateAnimationOn(Node node, double fromX, double toX, double duration){
            translate.setDuration(Duration.millis(duration));
            translate.setNode(node);
            translate.setFromX(fromX);
            translate.setToX(toX);
            translate.setCycleCount(1);
            translate.setAutoReverse(true);
            translate.play();
    }
    
    public void setTranslateResizeAnimationOn(Region region, double fromX, double toX, double duration, double sumWidth){
            transition.setTranslateResizeTransition(region, fromX, toX, duration, sumWidth);
            transition.play();
    }
    
    public void hambumguerAnimation(JFXHamburger node, double byAngle, double duration){
            rotate.setDuration(Duration.millis(duration));
            rotate.setNode(node);
            rotate.setByAngle(byAngle);
            rotate.setCycleCount(1);
            rotate.setAutoReverse(true);
            rotate.play();
    }
    
    public boolean isRunning(){
        if(rotate.getStatus() == Animation.Status.RUNNING || transition.getStatus() == Animation.Status.RUNNING 
        || translate.getStatus() == Animation.Status.RUNNING || scale.getStatus() == Animation.Status.RUNNING){
            
            return false;
        }
        else{
            return true;
        }
    }
    
    public void setScaleAnimation(Node node, double byX, double byY, double duration){
        scale.setDuration(Duration.millis(duration));
        scale.setNode(node);
        scale.setByX(byX);
        scale.setByY(byY);
        scale.setCycleCount(6);
        scale.setAutoReverse(true);
        scale.play();
    }
}
