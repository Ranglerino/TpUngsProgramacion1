package juego;


import entorno.Entorno;
import entorno.Herramientas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	// Variables y métodos propios de cada grupo
	private Nave miNave;
	private Asteroides[] listaAsteroides;
	private Proyectil Cohete;
	private Proyectil[] Listaiones;
	private navesDestructoras[] ListaNaves;
	private Boss jefeFinal;
	private boolean aparicionJefe;
	private item itemVida;
	private boolean Disparado;
	private boolean menu;
	private int score;
	private int vida;
	private boolean conVidas = true;
	private int cantEliminados;
	private int CantidadEnemigos;
	private int vidaJefe;
	private Image gameover;
	private Image winScreen;
	private Image background;
	private Image MenuImagen;
	private Image explosion;
	private ArrayList<Explosion> explosiones;
	
	private Proyectil disparoJefe;
	
	private Random Xrand;
    private Random Direccionrand ;


	
	// ...
	
	
	Juego()
	{
		
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Lost Galaxian - Grupo ... - v1", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		this.miNave = new Nave(400, 500, 60, 60);
		this.Cohete = new Proyectil (this.miNave.getX(),this.miNave.getY(),15,15,5,Color.RED);
		this.Disparado = true;
		
		
		this.menu = true;
		
		// Asteroides
		

		this.listaAsteroides =  new Asteroides[4];
		this.listaAsteroides[0] =  new Asteroides(100,1, 40, 1);
		this.listaAsteroides[1] =  new Asteroides(200,1, 40, 1);
		this.listaAsteroides[2] =  new Asteroides(400,1, 40, -1);
		this.listaAsteroides[3] =  new Asteroides(500,1, 40, -1);
		
		// Destructores Estelares (naves enemigas)
		this.ListaNaves = new navesDestructoras[4];
		this.ListaNaves [0] = new navesDestructoras(650,1,30,30,-1);
		this.ListaNaves [1] = new navesDestructoras(450,1,30,30,-1);
		this.ListaNaves [2] = new navesDestructoras(250,1,30,30,-1);
		this.ListaNaves [3] = new navesDestructoras(150,1,30,30,-1);


		//Disparo de naves enemigas
		this.Listaiones= new Proyectil[4];
		this.Listaiones[0] = new Proyectil(this.ListaNaves[0].getX(),this.ListaNaves[0].getY(),30,30,5,Color.BLUE);
		this.Listaiones[1] = new Proyectil(this.ListaNaves[1].getX(),this.ListaNaves[1].getY(),30,30,5,Color.BLUE);
		this.Listaiones[2] = new Proyectil(this.ListaNaves[2].getX(),this.ListaNaves[2].getY(),30,30,5,Color.BLUE);
		this.Listaiones[3] = new Proyectil(this.ListaNaves[3].getX(),this.ListaNaves[3].getY(),30,30,5,Color.BLUE);
		
		//JEFE FINAL
		this.aparicionJefe = true;
		
		
		
		
		//explosion
		this.explosion =Herramientas.cargarImagen("imagenes/explosion.gif");
		explosiones = new ArrayList<>();
		
		
		
		//musica
		Herramientas.loop("ost/mainTheme.wav");
	
		
		
		//imagenes
		this.gameover=Herramientas.cargarImagen("imagenes/gameover.png");
		this.background=Herramientas.cargarImagen("imagenes/background1.gif");
		this.MenuImagen=Herramientas.cargarImagen("imagenes/MenuImagen.jpg");
		this.winScreen = Herramientas.cargarImagen("imagenes/winScreen.jpg");
		
		//itemVida
		this.itemVida = new item(200,1, 40, 40);
		
		// DETERMINA LA CANTIDAD DE ENEMIGOS MAXIMOS POR PARTIDA
		this.CantidadEnemigos = 10;
		
		//vidas
		this.vida = 100;
		
		//vida jefe final
		this.vidaJefe = 350;
		
		//Score
		this.score =0;
		
		//Enemigos eliminados
		this.cantEliminados = 0;
		
		

		// ...

		// Inicia el juego!
		this.entorno.iniciar();
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		MenuInicial();
		if (this.conVidas && this.menu != true && this.score<10000) {
			
			// Procesamiento de un instante de tiempo
			this.entorno.dibujarImagen(background,400,300,0,1.6); // la imagen de fondo del juego
			
			//Nave Jugador
			miNave.dibujarNave(this.entorno);
			miNave.dibujarImagenNave(this.entorno);
			MovimientodeNave(); //todo el movimiento de la nave
			
			//score
			PuntosTotales(this.score); //dibuja en pantalla el puntaje
			
			//Cantidad de vida
			VidasTotal(this.vida); //dibuja en pantalla ela vida
			// Enemigos obliterados
			CantEnemigosEliminados(this.cantEliminados); //dibuja en pantalla el puntajla cantidad de enemigos eliminados
			//Dibujar explosion: Debido a que no se puede definir la cantidad de explosiones, decidimos usar un arrayList
			for (int i = 0; i < explosiones.size(); i++) {
				Explosion explosion = explosiones.get(i);
	            entorno.dibujarImagen(explosion.getImagen(), explosion.getX(), explosion.getY(), 0,0.5);
	            explosion.actualizar(); //Empieza a el transcurrir el tiempo
	            if (explosion.haTerminado()) { //llego al final de duracion
	                explosiones.remove(i); //lo elimino de la lista
	                i--;
	            }
	        }
			
			
			//Cuando el limite de enemigos sea 0, aparecera el jefe final
			if(this.CantidadEnemigos <=0) {
				if(this.aparicionJefe) {
					this.jefeFinal = new Boss(400,1, 170, 170);
					this.aparicionJefe = false;
				}
				this.jefeFinal.dibujarBoss(entorno);
				this.jefeFinal.dibujarImagenBoss(entorno);
				vidaDelJefe(this.vidaJefe);
				//el jefe llega hasta 1/4 del alto de la pantalla y luego se mueve en horizontal
				if(this.entorno.alto()/4 != this.jefeFinal.getY()) {
					this.jefeFinal.moverVertical();
				}else {
					this.jefeFinal.moverHorizontal();
				}//Cuando el jefe toque el borde de la pantalla se invierte su movimiento en vertical
				if(RebotarJefe(this.jefeFinal)) {
					this.jefeFinal.InvertirMoverHorizontal();
				} //Cuando muere el jefe final automaticamente ganas el juego
				if(this.vidaJefe<=0 ) {
					this.jefeFinal=null; //JEFE ELIMINADO
					this.score = 10000; //GANAS AUTOMATICAMENTE
				}
					 // si colisiona con la nave
					if(this.disparoJefe==null) {
						this.disparoJefe = new Proyectil(this.jefeFinal.getX(), this.jefeFinal.getY(), 30, 30, 5,Color.BLUE);
						
						
					}else {
						this.disparoJefe.moverAbajo();
						this.disparoJefe.dibujarProyectil(entorno);
						this.disparoJefe.dibujarImagenIones( entorno);
						
						
					}
					if (this.disparoJefe != null) { // si sale de la pantalla
					    if (this.disparoJefe.getY() >= this.entorno.alto()) {
					        this.disparoJefe = null;
					    }
					    
	
					}
					
					
				
				
				
				
			}
			
		
			
			this. Xrand = new Random();
			this. Direccionrand = new Random();
			//Movimiento Nave enemiga
			
				for (int i = 0; i< ListaNaves.length;i++) {
					if(this.ListaNaves[i] !=null) {
						this.ListaNaves[i].dibujarNaveEnemiga(this.entorno);
						this.ListaNaves[i].dibujarImagenNaveEnemiga(this.entorno);
						this.ListaNaves[i].mover();
						if(this.entorno.alto()/6 == this.ListaNaves[i].getY()) { //movimiento en zig zag
							this.ListaNaves[i].InvertirMovimiento();
						}
						if(this.entorno.alto()/3 == this.ListaNaves[i].getY()) { //movimiento en zig zag
							this.ListaNaves[i].InvertirMovimiento();
						}
						if(this.entorno.alto()/2 == this.ListaNaves[i].getY()) { //movimiento en zig zag
							this.ListaNaves[i].InvertirMovimiento();
						}
						if(this.ListaNaves[i].getY() > 600 ) { //sale de la pantalla
					    	this.ListaNaves[i]= null;
					    	if(this.CantidadEnemigos >=0) { // si ya se eliminaron todas las naves no crear mas
					    		this.ListaNaves[i]= new navesDestructoras(Xrand.nextInt(200,600),1,30,30,Direccionrand.nextBoolean() ? -1 : 1);
					    	}
					    	
						}
						if(RebotarNaveEnemiga(this.ListaNaves[i])) {
							this.ListaNaves[i].InvertirMovimiento();
							
						}
					}
	
					
				}
			
				
				//Disparo naveEnemiga
				
				for(int i=0; i<Listaiones.length;i++) {
					 // si colisiona con la nave
					if(this.Listaiones[i]==null) {
						generarIones(i); // Si es null crea un objeto iones
						colisiondeIones(this.Listaiones);
						
					}else {
						this.Listaiones[i].moverAbajo();
						this.Listaiones[i].dibujarProyectil(entorno);
						this.Listaiones[i].dibujarImagenIones( entorno);
						
					}
					if (this.Listaiones[i] != null) { // si sale de la pantalla
					    if (this.Listaiones[i].getY() >= this.entorno.alto()) {
					        this.Listaiones[i] = null;
					    }
					    
	
					}
					
					
				}
				//item de vidas
				this.itemVida.dibujarItem(entorno);
				this.itemVida.dibujarImagenItem(entorno);
				this.itemVida.mover();
				if(this.itemVida.getY() > 600 ) { //SI el item sale de la pantalla y no colisiona con la nave
			    	this.itemVida= null;
			    	this.itemVida= new item(Xrand.nextInt(50,550),1,40,40);
				
				}
				if(colisionItemNave(itemVida)) { // si el item colisiona con la nave
					Herramientas.play("ost/itemVida.wav");
					this.vida +=50; //le da 50 puntos de vida al jugador
					this.score +=10; // incrementa en 10 el puntaje
				}
					
				
				
					
				// Asteroides
				for (int i= 0; i <listaAsteroides.length;i++) {
					
					// Dibuja Asteroides
					if(this.listaAsteroides[i] !=null) {  // IMPORTANTE SI ES DESTRUIDO UN ASTEROIDE QUE NO LO ITERE
						this.listaAsteroides[i].dibujarAsteroide(entorno); 
						this.listaAsteroides[i].dibujarImagenAsteroide(entorno);
						this.listaAsteroides[i].mover();
					}// Mover asteroides y verificar colisión con la pantalla
					
					if (RebotarAsteroide(this.listaAsteroides[i])) { //Si el asteroide toca algun borde de la pantalla el asteroide rebota
					    	this.listaAsteroides[i].InvertirMovimiento();
						}
					   
					    if(this.listaAsteroides[i].getY() > 600 ) { //Si el asteroide sale de la pantalla
					    	this.listaAsteroides[i]= null;
					    	this.listaAsteroides [i]= new Asteroides(Xrand.nextInt(50,550),0,40,Direccionrand.nextBoolean() ? -1 : 1);
						
					}
					 
					    
					}
				//Movimiento
				// public final char TECLA_D = 68; FALTA
				//public final char TECLA_A = 65; FALTA
				
				//Disparo astro-megaship
				
				
				if (this.entorno.sePresiono(entorno.TECLA_ESPACIO) || this.Cohete.getY() != this.miNave.getY()) {
					this.Cohete.dibujarProyectil(entorno);
					this.Cohete.dibujarImagenCohete( entorno);
					this.Cohete.moverArriba(); //Cuando Se presiona el espacio el cohete sale disparado
					this.Disparado = false; // este boolean axuliar se pone en false
					
					//Colision cohete a asteroide	
				}if(colisionaAsteroideCohete(this.listaAsteroides, this.Cohete)) {
					this.Cohete =null; // el objeto se elimina
					this.Cohete = new Proyectil (this.miNave.getX(),this.miNave.getY(),15,15,5,Color.RED); //se crea uno nuevo
					this.Disparado = true;
					Herramientas.play("ost/Cohete.wav");
					this.score +=50;
					
				}//Colision cohete a Nave	
				if(colisionCoheteNave(this.ListaNaves)) {
					this.Cohete =null; // el objeto se elimina
					this.Cohete = new Proyectil (this.miNave.getX(),this.miNave.getY(),15,15,5,Color.RED); //se crea uno nuevo
					this.Disparado = true;
					Herramientas.play("ost/Cohete.wav");
					this.score +=150;
					this.cantEliminados +=1;
				}//Colision cohete a BOSS FINAL	
				if(colisionCoheteJefe(this.jefeFinal)) {
					this.vidaJefe -=50;
					this.Cohete =null; // el objeto se elimina
					this.Cohete = new Proyectil (this.miNave.getX(),this.miNave.getY(),15,15,5,Color.RED); //se crea uno nuevo
					this.Disparado = true;
					Herramientas.play("ost/Cohete.wav");
				}
				
				 //Cuando el Cohete sale de la pantalla se puede volver a disparar y no le pega a nada
					if(this.Cohete.getY()==0) { //Cuando el Cohete sale de la pantalla se puede volver a disparar y no le pega a nada
						this.Cohete =null; // el objeto se elimina
						this.Cohete = new Proyectil (this.miNave.getX(),this.miNave.getY(),15,15,5,Color.RED); //se crea uno nuevo
						this.Disparado = true;
						
					}
					
					//Colision Asteroides a Astro-MegaShip
				if(colisionaAsteroideNave(listaAsteroides) || colisiondeIones(Listaiones) || colisionNaveEnemigaANave(ListaNaves) || colisiondeIonesJefe(this.disparoJefe)) {
					Herramientas.play("ost/Ion.wav");
					this.miNave.dibujarImagenNaveDaño(entorno);
					this.vida -= 5;
					
				
					
						
					}
				if(this.vida <0) {
					this.conVidas = false;
				}
				
			}
				
			

		
		
		//Dibuja una pantalla al morir y al presionar espacio se detiene el programa
		if(this.conVidas != true) {
			PantallaPerder();
			if (this.entorno.sePresiono(entorno.TECLA_ESPACIO)) {
				System.exit(0);
				}
		}
		if(this.score ==10000) {
			PantallaGanar();
			if (this.entorno.sePresiono(entorno.TECLA_ESPACIO)) {
				System.exit(0);
				}
		}
		
		// ...
		

	}
	
	//Colision del item y la nave
		public boolean colisionItemNave(item itemVida) {
	        // Verificar si hay una colisión comparando las coordenadas y tamaños de los objetos
		            if (itemVida.getX() < miNave.getX() + miNave.getAncho() &&
		            	itemVida.getX() + itemVida.getAncho() > miNave.getX() &&
		            	itemVida.getY() < miNave.getY() + miNave.getAlto() &&
		            	itemVida.getY() + itemVida.getAlto() > miNave.getY()) {
		            	this.itemVida= null;
				    	this.itemVida= new item(Xrand.nextInt(50,550),1,40,40);
		                return true; // Hay una colisión
		            
				
			}
	        
	        return false; // No hay colisión
	    }
	
	
	
	
	//Colision Asteroides a Astro-MegaShip 
	public boolean colisionaAsteroideNave(Asteroides[] asteroide) {
        // Verificar si hay una colisión comparando las coordenadas y tamaños de los objetos
		
		for(int i=0; i< asteroide.length;i++) {
			if(this.listaAsteroides[i] !=null) { // IMPORTANTE SI ES DESTRUIDO UN ASTEROIDE QUE NO LO ITERE
				int asteroidX = asteroide[i].getX();
	            int asteroidY = asteroide[i].getY();
	            int asteroidRadio = asteroide[i].getRadio();
	            int naveX = miNave.getX();
	            int naveY = miNave.getY();
	            int naveAncho = miNave.getAncho();
	            int naveAlto = miNave.getAlto();

	            // Verificar si hay colisión comparando las coordenadas y tamaños de los objetos
	            if (naveX < asteroidX + asteroidRadio &&
	                naveX + naveAncho > asteroidX &&
	                naveY < asteroidY + asteroidRadio &&
	                naveY + naveAlto > asteroidY) {
	                return true; // Hay una colisión
	            }
				
			}
			
		}
        
        return false; // No hay colisión
    }
	
	//Colision Destructores estelares a Astro-MegaShip 
		public boolean colisionNaveEnemigaANave(navesDestructoras[] naveEnemiga) {
	        // Verificar si hay una colisión comparando las coordenadas y tamaños de los objetos
			
			for(int i=0; i< naveEnemiga.length;i++) {
				if(this.ListaNaves[i] !=null) { // IMPORTANTE SI ES DESTRUIDO UN ASTEROIDE QUE NO LO ITERE
					if (this.miNave.getX() < naveEnemiga[i].getX() + naveEnemiga[i].getAncho() &&
			        		this.miNave.getX() + this.miNave.getAncho() > naveEnemiga[i].getX() &&
			            this.miNave.getY() < naveEnemiga[i].getY() + naveEnemiga[i].getAlto() &&
			            this.miNave.getY() + this.miNave.getAlto() > naveEnemiga[i].getY()) {
						Explosion explosion = new Explosion(this.explosion, naveEnemiga[i].getX(), naveEnemiga[i].getY(), 30);
		                explosiones.add(explosion);
			            return true; // Hay una colisión
			        }
					
					
				}
				
			}
	        
	        return false; // No hay colisión
	    }
	
	//Colision Disparo de iones de Destructor estelar a Astro-MegaShip 
	public boolean colisiondeIones(Proyectil[] iones) {
        // Verificar si hay una colisión comparando las coordenadas y tamaños de los objetos
		
		for(int i=0; i< iones.length;i++) {
			if (iones[i] != null) {
	            if (iones[i].getX() < miNave.getX() + miNave.getAncho() &&
	                iones[i].getX() + iones[i].getAncho() > miNave.getX() &&
	                iones[i].getY() < miNave.getY() + miNave.getAlto() &&
	                iones[i].getY() + iones[i].getAlto() > miNave.getY()) {
	            	
	            	if(this.ListaNaves[i] !=null) {
	            		int xNaveEnemiga = this.ListaNaves[i].getX();
		            	int YNaveEnemiga = this.ListaNaves[i].getY();
		            	this.Listaiones[i]= null;
						this.Listaiones[i] = new Proyectil(xNaveEnemiga, YNaveEnemiga, 30, 30, 5,Color.BLUE);
						return true; // Hay una colisión
	            	}
	            	
	                
	            }
			}
		}
        
        return false; // No hay colisión
    }
	
	//Colision Disparo de iones del jefe a Astro-MegaShip 
		public boolean colisiondeIonesJefe(Proyectil iones) {
	        // Verificar si hay una colisión comparando las coordenadas y tamaños de los objetos
				if (iones != null) {
		            if (iones.getX() < miNave.getX() + miNave.getAncho() &&
		                iones.getX() + iones.getAncho() > miNave.getX() &&
		                iones.getY() < miNave.getY() + miNave.getAlto() &&
		                iones.getY() + iones.getAlto() > miNave.getY()) {
		            	
		            	if(this.jefeFinal !=null) {
			            	this.disparoJefe= null;
							this.disparoJefe= new Proyectil(this.jefeFinal.getX(), this.jefeFinal.getY(), 30, 30, 5,Color.BLUE);
							return true; // Hay una colisión
		            	}
		            	
		                
		            
				}
			}
	        
	        return false; // No hay colisión
	    }
	
	
	
	public boolean colisionaAsteroideCohete(Asteroides[] asteroide, Proyectil cohete) {
        // Verificar si hay una colisión comparando las coordenadas y tamaños de los objetos
		
		for(int i=0; i< asteroide.length;i++) {
			if(this.listaAsteroides[i] !=null) {  // IMPORTANTE SI ES DESTRUIDO UN ASTEROIDE QUE NO LO ITERE
		            int asteroidX = asteroide[i].getX();
		            int asteroidY = asteroide[i].getY();
		            int asteroidRadio = asteroide[i].getRadio();
		            int coheteX = cohete.getX();
		            int coheteY = cohete.getY();
		            int coheteAncho = cohete.getAncho();
		            int coheteAlto = cohete.getAlto();

		            // Verificar si hay colisión comparando las coordenadas y tamaños de los objetos
		            if (coheteX < asteroidX + asteroidRadio &&
		            	coheteX + coheteAncho > asteroidX &&
		            	coheteY < asteroidY + asteroidRadio &&
		            	coheteY + coheteAlto > asteroidY) {
		            	Explosion explosion = new Explosion(this.explosion, asteroidX, asteroidY, 30);
		                explosiones.add(explosion);
		            	this.listaAsteroides[i]= null;
					    this.listaAsteroides [i]= new Asteroides(Xrand.nextInt(50,550),0,40,Direccionrand.nextBoolean() ? -1 : 1);
					    
		                return true; // Hay una colisión
		            }
		            
		           
		             // Hay una colisión
		        }
			
			
		}
		
        
        return false; // No hay colisión
    }
	public boolean colisionCoheteNave(navesDestructoras[] naveEnemiga) {
        // Verificar si hay una colisión comparando las coordenadas y tamaños de los objetos
		
		for(int i=0; i< naveEnemiga.length;i++) {
			if(this.ListaNaves[i] !=null) { // IMPORTANTE SI ES DESTRUIDO UN ASTEROIDE QUE NO LO ITERE
				if (this.Cohete.getX() < naveEnemiga[i].getX() + naveEnemiga[i].getAncho() &&
		        		this.Cohete.getX() + this.Cohete.getAncho() > naveEnemiga[i].getX() &&
		            this.Cohete.getY() < naveEnemiga[i].getY() + naveEnemiga[i].getAlto() &&
		            this.Cohete.getY() + this.Cohete.getAlto() > naveEnemiga[i].getY()) {
					Explosion explosion = new Explosion(this.explosion, naveEnemiga[i].getX(), naveEnemiga[i].getY(), 30);
	                explosiones.add(explosion);
					this.ListaNaves[i]= null;
					this.CantidadEnemigos -=1;
					if(this.CantidadEnemigos >= 0 ) {
						this.ListaNaves[i]= new navesDestructoras(Xrand.nextInt(200,600),1,30,30,Direccionrand.nextBoolean() ? -1 : 1);
					}
			    	
		            return true; // Hay una colisión
		            
		            
		        }
				
				
			}
			
		}
        
        return false; // No hay colisión
    }
	
	public boolean hayColisionNavesEnemigas(navesDestructoras[] navesEnemigas) {
	    for (int i = 0; i < navesEnemigas.length - 1; i++) {
	        for (int j = i + 1; j < navesEnemigas.length; j++) {
	            if (navesEnemigas[i].colisionNaveEnemigaANave(navesEnemigas[j])) {
	                return true; // Se produce una colisión entre navesEnemigas[i] y navesEnemigas[j]
	            }
	        }
	    }
	    
	    return false; // No hay colisión entre ninguna pareja de naves enemigas
	}
	
	//Cuando el jefe  toca el borde de la ventana y cambia de direccion 
			private boolean RebotarJefe(Boss jefeFinal) { // Recibe un Objeto naveEnemiga y lo hace rebotar
					int limiteIzquierdo = 0;
					int limiteDerecho = this.entorno.ancho() -  jefeFinal.getAncho();
					 if (jefeFinal.getX() < limiteIzquierdo || jefeFinal.getX() > limiteDerecho) {
					       return true; // el jefe ha tocado el borde de la ventana
				}
				 
			return false; // No ha ocurrido colisión con el borde de la ventana
			
				
			}
			public boolean colisionCoheteJefe(Boss jefeFinal) {
		        // Verificar si hay una colisión comparando las coordenadas y tamaños de los objetos
						if(jefeFinal !=null) {
							if (this.Cohete.getX() < jefeFinal.getX() + jefeFinal.getAncho() &&
					        		this.Cohete.getX() + this.Cohete.getAncho() > jefeFinal.getX() &&
					            this.Cohete.getY() < jefeFinal.getY() + jefeFinal.getAlto() &&
					            this.Cohete.getY() + this.Cohete.getAlto() > jefeFinal.getY()) {
								Explosion explosion = new Explosion(this.explosion, jefeFinal.getX(), jefeFinal.getY()+130, 30);
				                explosiones.add(explosion);
								
						    	
					            return true; // Hay una colisión
						}
						
				            
				            
				        }
				
				
		        
		        return false; // No hay colisión
		    }
	
	
	
	//Cuando un Asteroide toca el borde de la ventana y cambia de direccion 
	private boolean RebotarAsteroide(Asteroides miAsteroide) { // Recibe un Objeto Asteroide y lo hace rebotar
		if (miAsteroide.getX() < 0 || miAsteroide.getX() > this.entorno.ancho() - 2 * miAsteroide.getRadio()) {
	        return true;
	    }
	    return false;
		
	}
	
	//Cuando una Nave enemiga toca el borde de la ventana y cambia de direccion 
		private boolean RebotarNaveEnemiga(navesDestructoras naveEnemiga) { // Recibe un Objeto naveEnemiga y lo hace rebotar
			if(naveEnemiga != null) {
				int limiteIzquierdo = 0;
				 int limiteDerecho = this.entorno.ancho() - 2 * naveEnemiga.getAncho();

				    if (naveEnemiga.getX() < limiteIzquierdo || naveEnemiga.getX() > limiteDerecho) {
				        return true; // La nave enemiga ha tocado el borde de la ventana
				    }
				
			}
			 

			    return false; // No ha ocurrido colisión con el borde de la ventana
			
		
			
		}
	

	
	private void MovimientodeNave() { //CONTROL DE LA NAVE
		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)
				&& this.miNave.getX() + this.miNave.getAncho() / 2 < this.entorno.ancho()) {
			this.miNave.moverDerecha();
			CoheteNave();
		}
		
		if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)
				&& this.miNave.getX() - this.miNave.getAncho() / 2 > 0) {
			this.miNave.moverIzquierda();
			CoheteNave();
		}
		
		
	}
	private void CoheteNave() {
		if(this.Cohete.getY() > 0  && Disparado) { // El cohete nos sigue cuando nos movemos mientras no sea disparado
			this.Cohete.setX(this.miNave.getX());
			this.Cohete.setY(this.miNave.getY());
		}
		
	}
	
	//Crea un nuevo proyectil Ion (NavesDestructoras)
	private void generarIones(int i) {
		if(this.ListaNaves[i] != null) {
			 this.Listaiones[i] = new Proyectil(this.ListaNaves[i].getX(), this.ListaNaves[i].getY(), 30, 30, 5,Color.BLUE);
		}
		
	    
	}
	
	
	
	//explosion
	
	
	
	//DIBUJA LAS PANTALLAS DEL JUEGO
	public void PantallaPerder() {
		this.entorno.dibujarImagen(gameover,400,300,0,1);
		this.entorno.cambiarFont(Font.DIALOG, 40, Color.RED);
		this.entorno.escribirTexto("PERDISTE",310,150);
		this.entorno.cambiarFont(Font.DIALOG, 30, Color.RED);
		this.entorno.cambiarFont(Font.DIALOG, 30, Color.RED);
		this.entorno.escribirTexto(" { PRESIONE ESPACIO PARA SALIR } ",140,550);
	}
	public void PantallaGanar() {
		this.entorno.dibujarImagen(winScreen,400,300,0,1);
		this.entorno.cambiarFont(Font.DIALOG, 40, Color.cyan);
		this.entorno.escribirTexto("GANASTE",310,150);
		this.entorno.cambiarFont(Font.DIALOG, 30, Color.cyan);
		this.entorno.cambiarFont(Font.DIALOG, 30, Color.cyan);
		this.entorno.escribirTexto(" { PRESIONE ESPACIO PARA SALIR } ",140,550);
	}
	
	public void PuntosTotales(int score) {
		this.entorno.cambiarFont(Font.SANS_SERIF, 30, Color.CYAN);
		this.entorno.escribirTexto("Puntos:"+ " " + score,570,590);
	}
	public void VidasTotal(int vida) {
		this.entorno.cambiarFont(Font.SANS_SERIF, 30, Color.CYAN);
		this.entorno.escribirTexto("❤:"+ " " + vida,100,590);
	}
	
	public void CantEnemigosEliminados(int cantEnemigos) {
		this.entorno.cambiarFont(Font.SANS_SERIF, 30, Color.CYAN);
		this.entorno.escribirTexto("Muertes:"+ " " + cantEnemigos,300,590);
	}
	
	public void vidaDelJefe(int vidaJefe) {
		this.entorno.cambiarFont(Font.SANS_SERIF, 30, Color.RED);
		this.entorno.escribirTexto("♛❤:"+ " " + vidaJefe,20,100);
	}
	
	public void MenuInicial() {
		
		if(this.menu) {
			this.entorno.dibujarImagen(MenuImagen,400,300,0);}
			if(this.entorno.sePresiono(this.entorno.TECLA_ENTER)) {
				this.menu = false;
			

		}
		
	}
	

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		int inicio = JOptionPane.showConfirmDialog(null, "¿Estás listo para acabar con esta amenaza? \n                               Presiona YES para comenzar \n \n INSTRUCTIVO: \n Presionando las tecla flechas de movimiento para moverte de izquierda a derecha \n Presionando la tecla SPACE: La nave dispara un proyectil \n   El juego termina al si no mueres o alcanzas los 10000 puntos \n    Si te enfrentas y ganas a la nave nodriza ganas automaticamente    \n                                      DISFRUTEN EL JUEGO \n                               \n","Simulador Lost Galaxian",JOptionPane.YES_OPTION);
		if (inicio==0) {
			Juego juego = new Juego();
			}
		else {
			System.exit(0);
			}
		
	}
}




