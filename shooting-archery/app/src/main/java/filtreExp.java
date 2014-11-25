/*
import android.graphics.Color;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
//import java.awt.MediaTracker.*;
import java.io.*;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.lang.Math;


class filtreExp
{
    public static boolean referencePoint(ArrayList <Color> pixels, Color treshold, int limit)
    {  
        for( int i=0; i<pixels.size(); i++)
        {
            if( ((limit<0) && 
                 (pixels.get(i).getRed() > treshold.getRed()) && 
                 (pixels.get(i).getGreen() > treshold.getGreen()) && 
                 (pixels.get(i).getBlue() > treshold.getBlue()))
                 ||
                ((limit>0) && 
                 (pixels.get(i).getRed() < treshold.getRed()) && 
                 (pixels.get(i).getGreen() < treshold.getGreen()) && 
                 (pixels.get(i).getBlue() < treshold.getBlue())) 
              )
            {
                return false;
            }
        }
        return true;
    }
      
    public static void main(String[] args)
    {
        if( args.length != 1 )
        {
            System.out.println("USAGE : java filtreExp.java photo.jpg");
            System.exit(-1);
        }
	    try 
	    {
            BufferedImage  img = ImageIO.read(new File(args[0]));

            Color tresholdBlack = new Color(135, 135, 135);
            Color tresholdWhite = new Color(115, 115, 115);
            Color tresholdRed = new Color(128, 127, 127);
            Color colorPixCurrent, colorPixPrecedent, colorPixSuivant;
            ArrayList <Color> pixels = new ArrayList<Color>();
            ArrayList <Color> pixelsReverse = new ArrayList<Color>(); 
            ArrayList <int[]> referencePoints = new ArrayList<int[]>();
            ArrayList <float[]> perpendicularPoints = new ArrayList<float[]>();
            ArrayList <float[]> averageCenterTarget = new ArrayList<float[]>();
            float xWidth, yHeight, xWidthCenter, yHeightCenter, xi, yi, xj, yj, xPerp, yPerp;
            float coefDir, ordOrigin, coefDirPerp;
            float coefDir1, coefDir2, ordOrigin1, ordOrigin2, ordOriginFinal, coefDirFinal;
            float xAverageCenterTarget, yAverageCenterTarget;
            int width = 1;
            int height = 1;
            int xCenter, yCenter;
            int nbPixDetect = 10; //nombre de pixels de détection
            boolean isRed;
            int sizeCircle = 0;
            
            
                    // A SUPPRIMER
                  Color colorGreen = new Color(0, 255, 0);
                  Color colorBlue = new Color(0, 0, 255);
                    // FIN A SUPPRIMER

            */
/** Block de la vérification diagonal de haut/bas, gauche/droite **//*

            for (height = 1; height < (img.getHeight()-1); height++)
            {
                try 
                {
                    colorPixCurrent = new Color(img.getRGB(width, height));
                    colorPixPrecedent = new Color(img.getRGB(width-1, height-1));
                    colorPixSuivant = new Color(img.getRGB(width+1, height+1));
                    
                    if( (colorPixCurrent.getRed() < tresholdBlack.getRed()) && 
                        (colorPixCurrent.getGreen() < tresholdBlack.getGreen()) && 
                        (colorPixCurrent.getBlue() < tresholdBlack.getBlue()) &&
                        (
                         (   (colorPixPrecedent.getRed() > tresholdWhite.getRed()) && 
                             (colorPixPrecedent.getGreen() > tresholdWhite.getGreen()) && 
                             (colorPixPrecedent.getBlue() > tresholdWhite.getBlue())
                         ) ||
                         (   (colorPixSuivant.getRed() > tresholdWhite.getRed()) && 
                             (colorPixSuivant.getGreen() > tresholdWhite.getGreen()) && 
                             (colorPixSuivant.getBlue() > tresholdWhite.getBlue())
                         )
                        )
                      )
                    {
                        for( int i=0; i<=nbPixDetect; i++ )
                        {
                            pixels.add( new Color(img.getRGB(width+i, height+i)) );
                            pixelsReverse.add( new Color(img.getRGB(width-i, height-i)) );
                        }

                        if( (referencePoint(pixels, tresholdBlack, -1)) || (referencePoint(pixelsReverse, tresholdBlack, -1)) )
                        {
                            referencePoints.add( new int[]{width, height} );
                        }
                    }

                    pixels.clear();
                    pixelsReverse.clear();
                    
                    width++;
                }
                catch( Exception e ){}
            }
            */
/** Endblock de la vérification diagonal de haut/bas, gauche/droite **//*

            
            */
/** Block de la vérification diagonal de haut/bas, droite/gauche **//*

            width = img.getWidth()-2;
            for (height = 1; height < (img.getHeight()-1); height++)
            {
                try
                {
                    colorPixCurrent = new Color(img.getRGB(width, height));
                    colorPixPrecedent = new Color(img.getRGB(width+1, height-1));
                    colorPixSuivant = new Color(img.getRGB(width-1, height+1));
                    
                    if( (colorPixCurrent.getRed() < tresholdBlack.getRed()) && 
                        (colorPixCurrent.getGreen() < tresholdBlack.getGreen()) && 
                        (colorPixCurrent.getBlue() < tresholdBlack.getBlue()) &&
                        (
                         (   (colorPixPrecedent.getRed() > tresholdWhite.getRed()) && 
                             (colorPixPrecedent.getGreen() > tresholdWhite.getGreen()) && 
                             (colorPixPrecedent.getBlue() > tresholdWhite.getBlue())
                         ) ||
                         (   (colorPixSuivant.getRed() > tresholdWhite.getRed()) && 
                             (colorPixSuivant.getGreen() > tresholdWhite.getGreen()) && 
                             (colorPixSuivant.getBlue() > tresholdWhite.getBlue())
                         )
                        )
                      )
                    {
                        for( int i=0; i<=nbPixDetect; i++ )
                        {
                            pixels.add( new Color(img.getRGB(width-i, height+i)) );
                            pixelsReverse.add( new Color(img.getRGB(width+i, height-i)) );
                        }

                        if( (referencePoint(pixels, tresholdBlack, -1)) || (referencePoint(pixelsReverse, tresholdBlack, -1)) )
                        {
                            referencePoints.add( new int[]{width, height} );
                        }
                    }

                    pixels.clear();
                    pixelsReverse.clear();
                    width--;
                }
                catch( Exception e ){}
            }
            */
/** Endblock de la vérification diagonal de haut/bas, droite/gauche **//*

            
            */
/** Block de la vérification horizontale **//*

            height = img.getHeight()/2;
            for (width = 1; width < (img.getWidth()-1); width++)
            {
                try
                {
                    colorPixCurrent = new Color(img.getRGB(width, height));
                    colorPixPrecedent = new Color(img.getRGB(width-1, height));
                    colorPixSuivant = new Color(img.getRGB(width+1, height));
                    
                    if( (colorPixCurrent.getRed() < tresholdBlack.getRed()) && 
                        (colorPixCurrent.getGreen() < tresholdBlack.getGreen()) && 
                        (colorPixCurrent.getBlue() < tresholdBlack.getBlue()) &&
                        (
                         (   (colorPixPrecedent.getRed() > tresholdWhite.getRed()) && 
                             (colorPixPrecedent.getGreen() > tresholdWhite.getGreen()) && 
                             (colorPixPrecedent.getBlue() > tresholdWhite.getBlue())
                         ) ||
                         (   (colorPixSuivant.getRed() > tresholdWhite.getRed()) && 
                             (colorPixSuivant.getGreen() > tresholdWhite.getGreen()) && 
                             (colorPixSuivant.getBlue() > tresholdWhite.getBlue())
                         )
                        )
                      )
                    { 
                        for( int i=0; i<=nbPixDetect; i++ )
                        {
                            pixels.add( new Color(img.getRGB(width+i, height)) );
                            pixelsReverse.add( new Color(img.getRGB(width-i, height)) );
                        }

                        if( (referencePoint(pixels, tresholdBlack, -1)) || (referencePoint(pixelsReverse, tresholdBlack, -1)) )
                        {
                            referencePoints.add( new int[]{width, height} );
                        }
                    }

                    pixels.clear();
                    pixelsReverse.clear();
                }
                catch( Exception e ){}
            }
            */
/** EndBlock de la vérification horizontale **//*

            
            */
/** Block de la vérification verticale **//*

            width = img.getWidth()/2;
            for(height = 1; height < (img.getHeight()-1); height++)
            {
                try
                {
                    colorPixCurrent = new Color(img.getRGB(width, height));
                    colorPixPrecedent = new Color(img.getRGB(width, height-1));
                    colorPixSuivant = new Color(img.getRGB(width, height+1));
                    
                    if( (colorPixCurrent.getRed() < tresholdBlack.getRed()) && 
                        (colorPixCurrent.getGreen() < tresholdBlack.getGreen()) && 
                        (colorPixCurrent.getBlue() < tresholdBlack.getBlue()) &&
                        (
                         (   (colorPixPrecedent.getRed() > tresholdWhite.getRed()) && 
                             (colorPixPrecedent.getGreen() > tresholdWhite.getGreen()) && 
                             (colorPixPrecedent.getBlue() > tresholdWhite.getBlue())
                         ) ||
                         (   (colorPixSuivant.getRed() > tresholdWhite.getRed()) && 
                             (colorPixSuivant.getGreen() > tresholdWhite.getGreen()) && 
                             (colorPixSuivant.getBlue() > tresholdWhite.getBlue())
                         )
                        )
                      )
                    {   
                        for( int i=0; i<=nbPixDetect; i++ )
                        {
                            pixels.add( new Color(img.getRGB(width, height+i)) );
                            pixelsReverse.add( new Color(img.getRGB(width, height-i)) );
                        }

                        if( (referencePoint(pixels, tresholdBlack, -1)) || (referencePoint(pixelsReverse, tresholdBlack, -1)) )
                        {
                            referencePoints.add( new int[]{width, height} );
                        }
                    }

                    pixels.clear();
                    pixelsReverse.clear();
                }
                catch( Exception e ){}
            }
            */
/** EndBlock de la vérification verticale **//*

            
            */
/** Block de calcul des milieux de segments et du coefDir & ordonnée à l'orgine de leur droite **//*

            */
/** Pour ce faire on prend le premier point de la liste et ont trace la perpendiculaire entre les autres points de la liste **//*

            for( int i=0; i<referencePoints.size(); i++ )
            {
                for( int j=(i+1); j<referencePoints.size(); j++ )
                {
                    xWidth = ( (float)(referencePoints.get(i)[0]) + (float)(referencePoints.get(j)[0]) )/2;
                    yHeight = ( (float)(referencePoints.get(i)[1]) + (float)(referencePoints.get(j)[1]) )/2;
                    
                    // Calcul du coeff directeur du segment référencé par les points i et j
                    xi = (float)(referencePoints.get(i)[0]);
                    yi = (float)(referencePoints.get(i)[1]);
                    xj = (float)(referencePoints.get(j)[0]);
                    yj = (float)(referencePoints.get(j)[1]);

                    coefDir = (yj-yi)/(xj-xi);
                    // Si la droite recherchée n'est pas une droite verticale
                    if( coefDir != 0 )
                    {
                        // Calcul du coef directeur et de l'ordonnée à l'origine de la perpendiculaire
                        coefDirPerp = (float)(-1.0/coefDir);
                        ordOrigin = yHeight - (coefDirPerp * xWidth);
                        
                        //Récupération de tous les points de la droite perpendiculaire
                        for(int k=0; k<img.getWidth(); k++)
                        {
                            xPerp = k;
                            yPerp = coefDirPerp*k + ordOrigin;
                            
                            if( (yPerp < img.getHeight()) && (yPerp > 0))
                            {
                                perpendicularPoints.add( new float[]{xPerp, yPerp} );    
                            }
                        }
                    }
                    else
                    {
                        //Récupération de tous les points de la droite perpendiculaire
                        xPerp = xWidth;
                        for(int k=0; k<img.getHeight(); k++)
                        {
                            yPerp = k;
                            
                            if( (yPerp < img.getHeight()) && (yPerp > 0))
                            {
                                perpendicularPoints.add( new float[]{xPerp, yPerp} );
                            }
                        }
                    }
                }
            }
            */
/** EndBlock de calcul des milieux de segments et du coefDir & ordonnée à l'orgine de leur droite **//*

            
            */
/** Block pour trouver le centre de la cible **//*

            int nbSamePoints = 0;
            int nbSamePointsAvant = 0;
            float[] targetCenter = new float[]{0,0};
            for( int k=0; k<perpendicularPoints.size(); k++ )
            {
                for( int l=0; l<perpendicularPoints.size(); l++ )
                {
                    xi = (int)(perpendicularPoints.get(k)[0]);
                    xj = (int)(perpendicularPoints.get(k)[1]);
                    yi = (int)(perpendicularPoints.get(l)[0]);
                    yj = (int)(perpendicularPoints.get(l)[1]);
                    if( (k != l) &&
                        ((xi==yi) && (xj==yj)) || 
                        ((xi==yi+1) && (xj==yj)) ||
                        ((xi==yi+1) && (xj==yj+1)) ||
                        ((xi==yi+1) && (xj==yj-1)) ||
                        ((xi==yi) && (xj==yj+1)) ||
                        ((xi==yi) && (xj==yj-1)) ||
                        ((xi==yi-1) && (xj==yj)) ||
                        ((xi==yi-1) && (xj==yj+1)) ||
                        ((xi==yi-1) && (xj==yj-1)) ||
                        ((xi==yi+2) && (xj==yj)) ||
                        ((xi==yi+2) && (xj==yj+1)) ||
                        ((xi==yi+2) && (xj==yj+2)) ||
                        ((xi==yi+2) && (xj==yj-1)) ||
                        ((xi==yi+2) && (xj==yj-2)) ||
                        ((xi==yi+1) && (xj==yj+2)) ||
                        ((xi==yi+1) && (xj==yj-2)) ||
                        ((xi==yi) && (xj==yj+2)) ||
                        ((xi==yi) && (xj==yj-2)) ||
                        ((xi==yi-1) && (xj==yj+2)) ||
                        ((xi==yi-1) && (xj==yj-2)) ||
                        ((xi==yi-2) && (xj==yj)) ||
                        ((xi==yi-2) && (xj==yj+1)) ||
                        ((xi==yi-2) && (xj==yj+2)) ||
                        ((xi==yi-2) && (xj==yj-1)) ||
                        ((xi==yi-2) && (xj==yj-2))
                      )
                    {
                        nbSamePoints++;    
                    }
                }
                if( nbSamePoints > nbSamePointsAvant )
                {
                    nbSamePointsAvant = nbSamePoints;
                    targetCenter[0] = perpendicularPoints.get(k)[0];
                    targetCenter[1] = perpendicularPoints.get(k)[1];
                }
                
                nbSamePoints = 0;
            }
            
            System.out.println( targetCenter[0] +","+ targetCenter[1]+" : "+nbSamePointsAvant );
            img.setRGB((int)(targetCenter[0]), (int)(targetCenter[1]), colorBlue.getRGB());
            img.setRGB((int)(targetCenter[0]+1), (int)(targetCenter[1]), colorBlue.getRGB());
            img.setRGB((int)(targetCenter[0]-1), (int)(targetCenter[1]), colorBlue.getRGB());
            img.setRGB((int)(targetCenter[0]), (int)(targetCenter[1]-1), colorBlue.getRGB());
            img.setRGB((int)(targetCenter[0]), (int)(targetCenter[1]+1), colorBlue.getRGB());            
            */
/** FIN Block pour trouver le centre de la cible **//*

            
            */
/** Block de calcul du positionnement des cercles de couleur **//*

            xCenter = (int)(targetCenter[0]);
            yCenter = (int)(targetCenter[1]);
            pixels.clear();
            for( int i=1; i<(img.getHeight()/2); i++ )
            {
                //Lorsque l'on passe au rouge alors on a trouvé la largeur de chaque bande de couleur
                colorPixCurrent = new Color(img.getRGB(xCenter, yCenter+i));
                
                if( (colorPixCurrent.getRed() > tresholdRed.getRed()) && 
                    (colorPixCurrent.getGreen() < tresholdRed.getGreen()) && 
                    (colorPixCurrent.getBlue() < tresholdRed.getBlue())
                  )
                {
                    try
                    {
                        for( int j=0; j<=nbPixDetect; j++ )
                        {
                            pixels.add( new Color(img.getRGB(xCenter, yCenter+i+j)) );
                        }

                        isRed = true;
                        for( int j=0; j<pixels.size(); j++)
                        {
                            if(  (pixels.get(j).getRed() < tresholdRed.getRed()) && 
                                 (pixels.get(j).getGreen() > tresholdRed.getGreen()) && 
                                 (pixels.get(j).getBlue() > tresholdRed.getBlue())
                              )
                            {
                                isRed = false;
                            }
                        }

                        if( isRed )
                        {
                            sizeCircle = i;
                            i = img.getHeight()/2;
                            
                            //A SUPPRIMER
                            for( int z=0; z<sizeCircle; z++ )
                            {
                                img.setRGB(xCenter, yCenter+z, colorGreen.getRGB());
                            }
                        }
                    }
                    catch( Exception e )
                    {
                        System.out.println("Erreur dans la détection des cercles : " + e );
                    }
                }
                
                pixels.clear();
            }
            
            System.out.println("Taille d'un cercle de couleur : " + sizeCircle);
            */
/** Block de calcul du positionnement des cercles de couleur **//*

            
            
            
            // enregistrement de la nouvelle image
            ImageIO.write(img, "jpg",new File("newCible.jpg"));
      }	
      catch (Exception e) 
      {
	      System.err.println("erreur -> "+e.getMessage());
      }
      
      System.out.println("fin");		
    }	
}
*/
