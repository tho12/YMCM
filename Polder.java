package amstelhaege;

import java.util.Random;

public class Polder {
	final int POLDER_WIDTH = 1700;
	final int POLDER_HEIGHT = 2000;

	final int TOTAL = 100;
	final double PERC_FAM = 0.5;
	final double PERC_BUNG = 0.3;
	final double PERC_MANS = 0.2;
	final double MIN_CLEAR_FAM = 2;
	final double MIN_CLEAR_BUNG = 3;
	final double MIN_CLEAR_MANS = 6;
	final double PRICE_FAM = 285000;
	final double PRICE_BUNG = 399000;
	final double PRICE_MANS = 610000;
	final double PRICE_INCREASE_FAM = 0.03;
	final double PRICE_INCREASE_BUNG = 0.04;
	final double PRICE_INCREASE_MANS = 0.06;



	final int HOUSE=1;
	final int WATER=2;
	final int CLEARANCE = 3;
	final int NOTHING=0;

	int[][] world_matrix;
	int[][] init_environment;

	Random rand = new Random();

	Polder(){
		//	int size=WORLD_SIZE;
		int height = POLDER_HEIGHT;
		int width = POLDER_WIDTH;
		world_matrix= new int[width][height];
		//	pop=ORG_POP;
		//	gen=1;
		//	iterations=0;
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				world_matrix[i][j]= 0;
			}
		}
		generatePlaceHouse();

		//	generateFood();
	}

	//	init_environment=copy_world(world_matrix);
	// Heuristic, check one house
	//	generateOrganisms();
	//	PlaceOrganisms();
	// update houses


	int[][] copy_world(int[][] original){
		int[][] copy = new int[POLDER_WIDTH][POLDER_HEIGHT];
		for(int i = 0; i<POLDER_WIDTH; i++) {
			for(int j = 0; j<POLDER_HEIGHT; j++) {
				int object = original[i][j];
				copy[i][j] = object;
			}
		}
		return(copy);
	}

	void generatePlaceHouse() {
		int placedNum = 0;
		int mansions = 0;
		int bungalows = 0;
		int fam = 0;
		int maxNum = TOTAL;
		while(placedNum < maxNum) {
			//gen random coordinate
			int x = rand.nextInt(POLDER_WIDTH);
			int y = rand.nextInt(POLDER_HEIGHT);
			String lining1, lining2;
			if(Math.random() > 0.5) {
				lining1 = "vertical";
				lining2 = "horizontal";
			} else {
				lining1 = "horizontal";
				lining2 = "vertical";
			}
			
			if(mansions < TOTAL * PERC_MANS) {
				if(genMansion(x,y, lining1)) {
					placedNum ++;
					mansions++;
				} else if(genMansion(x,y, lining2)) {
					placedNum ++;
					mansions++;
				}
			}
			else if(bungalows < TOTAL * PERC_BUNG) {
				if(genBungalow(x,y, lining1)) {
					placedNum++;
					bungalows++;
				} else if (genBungalow(x,y, lining2)) {
					placedNum++;
					bungalows++;
				}
			}
			else if(fam < TOTAL * PERC_FAM) {
				if(genFamHouse(x,y)) {
					placedNum++;
					fam++;
				}
			}
		}

	}

	boolean outOfBounds(int x, int y) {
		return (x < 0 || x > POLDER_WIDTH-1 || y < 0 || y > POLDER_HEIGHT-1);
	}

	boolean notOnHouse(int x, int y) {
		if(outOfBounds(x,y)) {
			return false;
		} else {
			return (world_matrix[x][y] != HOUSE);
		}
	}

	boolean legalProperty(int startX, int startY, int len1, int len2) {
		for(int i = startX; i < len1+startX; i++) {
			for(int j = startY; j < len2+startY; j++) {
				if(!outOfBounds(i,j)) {
					if(world_matrix[i][j] != NOTHING) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	boolean genMansion(int startX, int startY, String lining) {
		boolean placed = false;
		int len1;
		int len2;
		
		if(lining.contains("vertical")) {
			len1 = 105;
			len2 = 110;
		} else {
			len1 = 110;
			len2 = 105;
		}
		int clearance = 60;

		//get random coordinates
		//check possibility

		if(legalProperty(startX,startY,len1,len2)) {
			placed = true;
		}


		if(placed) {
			//		House newHouse = new House();
			for(int i = startX; i < len1+startX; i++) {
				for(int j = startY; j < len2+startY; j++) {
					if(!outOfBounds(i,j)) {
						world_matrix[i][j] = HOUSE;
						// sla coordinaat op?
					} else {
						placed = false;
						System.out.print("not possible to place house");
					}
				}
			}

			// hier wordt clearance berekend en geplaatst.
			for(int h = 0; h < clearance; h++) {

				for(int k = startX-h; k < startX+h+len1; k++) {
					if(notOnHouse(k, startY - h-1)) {
						world_matrix[k][startY-h-1] = CLEARANCE;
					}

					if(notOnHouse(k, startY+len2+h+1)) {
						world_matrix[k][startY+len2+h+1] = CLEARANCE;
					}

				}

				for(int l = startY-h; l < startY+h+len2; l++) {
					if(notOnHouse(startX-h-1, l)) {
						world_matrix[startX-h-1][l] = CLEARANCE;
					}

					if(notOnHouse(startX+len1+h, l)) {
						world_matrix[startX+len1+h][l] = CLEARANCE;
					}
				}
			}
		}
		return placed;
	}
	
	boolean genBungalow(int startX, int startY, String lining) {
		boolean placed = false;
		int len1;
		int len2;
		
		if(lining.contains("vertical")) {
			len1 = 75;
			len2 = 100;
		} else {
			len1 = 100;
			len2 = 75;
		}

		int clearance = 30;

		//get random coordinates
		//check possibility

		if(legalProperty(startX,startY,len1,len2)) {
			placed = true;
		}


		if(placed) {
			//		House newHouse = new House();
			for(int i = startX; i < len1+startX; i++) {
				for(int j = startY; j < len2+startY; j++) {
					if(!outOfBounds(i,j)) {
						world_matrix[i][j] = HOUSE;
						// sla coordinaat op?
					} else {
						placed = false;
						System.out.print("not possible to place house");
					}
				}
			}

			// hier wordt clearance berekend en geplaatst.
			for(int h = 0; h < clearance; h++) {

				for(int k = startX-h; k < startX+h+len1; k++) {
					if(notOnHouse(k, startY - h-1)) {
						world_matrix[k][startY-h-1] = CLEARANCE;
					}

					if(notOnHouse(k, startY+len2+h+1)) {
						world_matrix[k][startY+len2+h+1] = CLEARANCE;
					}

				}

				for(int l = startY-h; l < startY+h+len2; l++) {
					if(notOnHouse(startX-h-1, l)) {
						world_matrix[startX-h-1][l] = CLEARANCE;
					}

					if(notOnHouse(startX+len1+h, l)) {
						world_matrix[startX+len1+h][l] = CLEARANCE;
					}
				}
			}
		}
		return placed;
	}
	
	boolean genFamHouse(int startX, int startY) {
		boolean placed = false;
		int len1 = 80;
		int len2 = 80;
		int clearance = 20;

		//get random coordinates
		//check possibility

		if(legalProperty(startX,startY,len1,len2)) {
			placed = true;
		}


		if(placed) {
			//		House newHouse = new House();
			for(int i = startX; i < len1+startX; i++) {
				for(int j = startY; j < len2+startY; j++) {
					if(!outOfBounds(i,j)) {
						world_matrix[i][j] = HOUSE;
						// sla coordinaat op?
					} else {
						placed = false;
						System.out.print("not possible to place house");
					}
				}
			}

			// hier wordt clearance berekend en geplaatst.
			for(int h = 0; h < clearance; h++) {

				for(int k = startX-h; k < startX+h+len1; k++) {
					if(notOnHouse(k, startY - h-1)) {
						world_matrix[k][startY-h-1] = CLEARANCE;
					}

					if(notOnHouse(k, startY+len2+h+1)) {
						world_matrix[k][startY+len2+h+1] = CLEARANCE;
					}

				}

				for(int l = startY-h; l < startY+h+len2; l++) {
					if(notOnHouse(startX-h-1, l)) {
						world_matrix[startX-h-1][l] = CLEARANCE;
					}

					if(notOnHouse(startX+len1+h, l)) {
						world_matrix[startX+len1+h][l] = CLEARANCE;
					}
				}
			}
		}
		return placed;
	}

	boolean checkProperty(int startX, int startY, int len1, int len2) {
		// vertically outlined

		for(int i = startX; i < startX+len1; i++) {
			for(int j = startY; i < startY+len2; j++) {
				if(world_matrix[i][j] == HOUSE || world_matrix[i][j] == WATER || world_matrix[i][j] == CLEARANCE) {
					return false;
				}
			}

		}
		return true;
	}
	
	double getValue() {
		return .1;
	}

}
