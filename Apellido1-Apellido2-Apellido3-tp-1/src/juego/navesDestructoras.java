package juego;

import java.awt.Color;

import entorno.Entorno;

public class navesDestructoras {
	private int x;
		private int y;
	private int ancho;
	private int alto;
	private int velocidad;
	private int direccion;
	
	
	
	navesDestructoras(int x, int y, int ancho, int alto){
		this.x = x;
		this.y = y;
		this.alto = alto;
		this.ancho = ancho;
		this.velocidad = 1;
		this.direccion= 1;
		
		
		
	}
	
	
	
	
	
	// Dibuja la nave
		void dibujarNaveEnemiga(Entorno entorno) {
			entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.GRAY);
			
		}
		
		void moverVertical() {
			this.y += this.velocidad;
		}
		void moverDiagonal() {
			this.y += this.velocidad;
			this.x += this.direccion;
		}
		public void InvertirMovimiento() {
			this.velocidad = (-velocidad);
			this.direccion =(-direccion);
					
		}
		void invertirDireccion() {
			this.direccion= (-direccion);
		}
		
		
		
		
		
		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}


		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getAncho() {
			return ancho;
		}


		public void setAncho(int ancho) {
			this.ancho = ancho;
		}

		public int getAlto() {
			return alto;
		}

		public void setAlto(int alto) {
			this.alto = alto;
		}

		public int getVelocidad() {
			return velocidad;
		}

		public void setVelocidad(int velocidad) {
			this.velocidad = velocidad;
		}

		public int getDireccion() {
			return direccion;
		}

		public void setDireccion(int direccion) {
			this.direccion = direccion;
		}
	
	
	}


