package com.example.application;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class ImageLoader {
     private final ArrayList<CC> palette = new ArrayList<>();
        private final ArrayList<CoOrd> count = new ArrayList<>();
        private int DIFFERENCE = 30;
        private int NUMBER = 10;

        public ImageLoader(String urlPath, int num, int dif){
            try {
                URL url = new URL(urlPath);
                NUMBER = num;
                DIFFERENCE = dif;
                BufferedImage in = ImageIO.read(url);
                paletteSetup(in);
            } catch (IOException e) {
                System.out.println("Loading Error");
                e.printStackTrace();
            }
        }
        void paletteSetup(BufferedImage img){
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    Color color = new Color(img.getRGB(x, y), true);

                    if (checkCol(color)){
                        CC n = new CC(color);
                        palette.add(n);
                        count.add(new CoOrd(palette.size()-1));
                    }
                }
            }
        }

        double checkDif(int x1, int y1, int z1, int x2, int y2, int z2){
            return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - z1), 2));
        }
        boolean checkCol(Color color){
            int i = palette.size() - 1;
            while (i > 0){
                double dif = checkDif(color.getRed(), color.getGreen(), color.getBlue(), palette.get(i).getR(),
                        palette.get(i).getG(), palette.get(i).getB());
                if (dif <= DIFFERENCE){
                    count.get(i).add();
                    return false;
                }
                i--;
            }
            return true;
        }

       public ArrayList<Color> PaletteLoader() {
            sort(count);
            ArrayList<Color> finale = new ArrayList<>();
            for (int x = count.size()-1; x >= count.size()-NUMBER; x--){
                Color c = palette.get(count.get(x).getI()).getC();
                finale.add(c);
            }
            return finale;
        }

        public static void sort(ArrayList<CoOrd> list) {
            list.sort(Comparator.comparing(CoOrd::getCount));
        }

}

