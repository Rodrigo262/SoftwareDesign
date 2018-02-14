define(['ojs/ojcore', 'knockout', 'jquery', 'appController', 'ojs/ojinputtext'],
 function(oj, ko, $, app) {  /////
    function LoginViewModel() {
    var self=this;
   this.nombreDeUsuario=ko.observable("MANUEL");
   this.pwd=ko.observable("1234");
   this.juego=ko.observable("Tres En Raya");
   this.entrar=function(){
     var p={
       tipo: "login",
       jugador:this.nombreDeUsuario(),
       pwd: this.pwd(),
       juego: "Tres En Raya"
     };
    
    $.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/Logout.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
      if(respuesta.tipo=="OK") {
        sessionStorage.jugador=self.nombreDeUsuario();
        //app.router.go("juegos");
        window.location = "index.html"//hacerlo con javascript
        alert("Bienvenido");
      } else
        alert(respuesta.texto);
    });
  }
}
    return new LoginViewModel();
  }
);