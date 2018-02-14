var websocket;
define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojknockout', 'promise',
	'ojs/ojdatagrid', 'ojs/ojarraydatagriddatasource'],
 function(oj, ko, $, app) { /////
    function JuegosViewModel(){

      var self=this;
      self.nombreDeUsuario=ko.observable(sessionStorage.jugador);
      //alert(nombreDeUsuario);
      //self.tablero=ko.observable([
        //[' ',' ',' '],
        //[' ',' ',' '],
        //[' ',' ',' ']
      //]);
	    //self.casillas = ko.observable(new oj.ArrayDataGridDataSource(self.tablero));
      self.oponente= ko.observable(sessionStorage.oponente);
      self.estado= ko.observable(" ");
      
  var url="ws://localhost:8080/ServidorJuego/WSServer?jugador=" + self.nombreDeUsuario();
  websocket = new WebSocket(url);

  websocket.onopen=function(){
    self.estado("Conectado (WS)");
  }
  websocket.onmessage=function(mensaje){
  	var mensaje = JSON.parse(mensaje.data);
  	if(mensaje.tipo=="CrearPartida"){
  		var j1 = mensaje.jugador1;
  		var j2 = mensaje.jugador2;
  		var jt = mensaje.jugadorConElTurno;
  		var id = mensaje.IdPartida;
  		var texto = "\t\t\t\tId Partida: "+ id+"\nJugador 1 : "+j1 + "\nJugador 2: "+ j2 + "\nTurno de : "+jt;
  		alert(texto);
  	}
    if(mensaje.tipo=="movimiento"){
      var ficha=mensaje.ficha;
      var fila=mensaje.fila;
      var col=mensaje.columna;
      actualizar(fila, col, ficha);

    }
    if(mensaje.tipo=="turno"){
      var jugador=mensaje.turno;
      var texto= "El jugador con el turno es: " + jugador; 
      alert(texto);
    }
    if(mensaje.tipo=="victoria"){
      var jugador = mensaje.jugador;
      var texto =  "Ha ganado el jugador: " + jugador;
      alert (texto);
      window.location = "index.html?root=ter";
    }
    if(mensaje.tipo=="empate"){
      var texto= "Ha habido un empate";
      alert(texto);
      window.location = "index.html?root"
    }
    if(mensaje.tipo =="legalidad"){
      var error=mensaje.error;
      alert(error);
    }
  }

  function actualizar(fila, columna, marca){
    if(fila == 0 && columna==0){
      document.getElementById("button1").innerHTML=marca;
    }
    if(fila == 0 && columna==1){
      document.getElementById("button2").innerHTML=marca;
    }
    if(fila == 0 && columna==2){
      document.getElementById("button3").innerHTML=marca;
    }
    if(fila == 1 && columna==0){
      document.getElementById("button4").innerHTML=marca;
    }
    if(fila == 1 && columna==1){
      document.getElementById("button5").innerHTML=marca;
    }
    if(fila == 1 && columna==2){
      document.getElementById("button6").innerHTML=marca;
    }
    if(fila == 2 && columna==0){
      document.getElementById("button7").innerHTML=marca;
    }
    if(fila == 2 && columna==1){
      document.getElementById("button8").innerHTML=marca;
    }
    if(fila == 2 && columna==2){
      document.getElementById("button9").innerHTML=marca;
    }
  }

  websocket.onclose=function(){
    self.estado("Desconectado");
  }
  websocket.onerror=function(){
    self.estado("Error conectando al websocket");
  }

     this.logout=function(){
     var p={
       tipo: "Logout",
       jugador:this.nombreDeUsuario()
     };
    
    $.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/Logout.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
      if(respuesta.tipo=="OK") {
        window.location = "index.html"
      } else
        alert(respuesta.texto);
    });
    }
    this.uno=function(){
      var filaSeleccion = 0;
      var columnaSeleccion = 0;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.dos=function(){
      filaSeleccion= 0;
      columnaSeleccion= 1;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.tres=function(){
      filaSeleccion= 0;
      columnaSeleccion=2;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.cuatro=function(){
      filaSeleccion= 1;
      columnaSeleccion=0;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.cinco=function(){
      filaSeleccion= 1;
      columnaSeleccion=1;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.seis=function(){
      filaSeleccion= 1;
      columnaSeleccion=2;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.siete=function(){
      filaSeleccion= 2;
      columnaSeleccion=0;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.ocho=function(){
      filaSeleccion= 2;
      columnaSeleccion=1;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.nueve=function(){
      filaSeleccion= 2;
      columnaSeleccion=2;
      jugador = this.nombreDeUsuario()
      movimientos(filaSeleccion, columnaSeleccion, jugador);
    }
    this.bR=function(){
      var p={
       tipo: "Ranking",
       jugador: this.nombreDeUsuario()
     };
    
    $.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/Ranking.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
    	var mensaje = JSON.parse(respuesta);
    	$.each(mensaje, function(i,item){
			document.write(i+" - "+mensaje[i].nombre+" - "+mensaje[i].victorias);
		})
    });
      
    }
    function movimientos(columnaSeleccion, filaSeleccion, jugador){
      var p={
       tipo: "RecibirMovimiento",
       jugador: jugador,
       columna: columnaSeleccion, 
       fila: filaSeleccion
     };
    
    $.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/RecibirMovimiento.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
      if(respuesta.tipo=="OK") {
        

      } else
        alert(respuesta.texto);
    });
    }
  
}
    return new JuegosViewModel();
  }
);
