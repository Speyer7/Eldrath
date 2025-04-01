package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];


    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/resources/maps/World1/map3.txt");
    }

    public void loadMap(String filePath) {

        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while(col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public void getTileImage() {

        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResource("/resources/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResource("/resources/tiles/wall.png"));
            tile[1].collisionOn = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResource("/resources/tiles/water.png"));
            tile[2].collisionOn = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResource("/resources/tiles/dirt.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResource("/resources/tiles/tree.png"));
            tile[4].collisionOn = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResource("/resources/tiles/road00.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.PLAYER.worldX + gp.PLAYER.screenX;
            int screenY = worldY - gp.PLAYER.worldY + gp.PLAYER.screenY;

            if(worldX + gp.tileSize > gp.PLAYER.worldX - gp.PLAYER.screenX &&
               worldX - gp.tileSize< gp.PLAYER.worldX + gp.PLAYER.screenX &&
               worldY + gp.tileSize> gp.PLAYER.worldY - gp.PLAYER.screenY &&
               worldY - gp.tileSize< gp.PLAYER.worldY + gp.PLAYER.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
