package graph;
import graph.WeightedGraph;
import maze.Juncture;
import maze.Maze;

public class MazeGraph extends WeightedGraph<Juncture> {

	public MazeGraph(Maze maze) {

		int width = maze.getMazeWidth(); 
		int height = maze.getMazeHeight(); 

		Juncture [] [] allJunctures = new Juncture [width] [height]; // stores the value of the Junctures 

		for (int xCoordinate = 0; xCoordinate < width; xCoordinate++) {

			for (int yCoordinate = 0; yCoordinate < height; yCoordinate++) {

				Juncture currJuncture = new Juncture (xCoordinate, yCoordinate); 
				this.addVertex(currJuncture); 

				allJunctures [xCoordinate] [yCoordinate] = currJuncture; // populates the 2d Array 
			}	
		}

		for (int xCoordinate = 0; xCoordinate < width; xCoordinate++) {

			for (int yCoordinate = 0; yCoordinate < height; yCoordinate++) { // traverses through the 2d Array 

				Juncture tempJuncture = allJunctures [xCoordinate] [yCoordinate]; 

				if (!maze.isWallAbove(tempJuncture) && yCoordinate > 0) { // above 

					this.addEdge(tempJuncture, allJunctures [xCoordinate] [yCoordinate - 1] , 
							maze.getWeightAbove(tempJuncture));
				}

				if (!maze.isWallBelow(tempJuncture) && yCoordinate < height - 1) { // below 

					this.addEdge(tempJuncture, allJunctures [xCoordinate] [yCoordinate + 1] , 
							maze.getWeightBelow(tempJuncture));
				}

				if (!maze.isWallToLeft(tempJuncture) && xCoordinate > 0) { // left 

					this.addEdge(tempJuncture, allJunctures [xCoordinate - 1] [yCoordinate] , 
							maze.getWeightToLeft(tempJuncture));
				}

				if (!maze.isWallToRight(tempJuncture) && xCoordinate  < width - 1) { // right 

					this.addEdge(tempJuncture, allJunctures [xCoordinate + 1 ] [yCoordinate] , 
							maze.getWeightToRight(tempJuncture));

				}

			}

		}

	}
}
