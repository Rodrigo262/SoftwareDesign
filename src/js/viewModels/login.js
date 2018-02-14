define(['ojs/ojcore', 'knockout', 'jquery', 'appController','ojs/ojinputtext','ojs/ojknockout', 'ojs/ojselectcombobox'],
 function(oj, ko, $, app) {
    function LoginViewModel() {
    var self=this;
   this.nombreDeUsuario=ko.observable("MANUEL");
   this.pwd=ko.observable("1234");
   
   this.entrarTER=function(){
     var p={
       tipo: "Login",
       jugador:this.nombreDeUsuario(),
       pwd: this.pwd(),
       juego: "TresEnRaya"
     };
    
    $.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/Login.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
      if(respuesta.tipo=="OK") {
        sessionStorage.jugador=self.nombreDeUsuario();
        window.location = "index.html?root=ter"
       
      } else
        alert(respuesta.texto);
    });
    }
    this.entrarHLF=function(){
     var p={
       tipo: "Login",
       jugador:this.nombreDeUsuario(),
       pwd: this.pwd(),
       juego: "HundirLaFlota"
     };
    
    $.ajax({
      type: "POST",
      url: "http://127.0.0.1:8080/ServidorJuego/Login.jsp",
      data: "p="+ JSON.stringify(p),
    }).done(function(respuesta){
      if(respuesta.tipo=="OK") {
        sessionStorage.jugador=self.nombreDeUsuario();
        window.location = "index.html?root=hf"
       
      } else
        alert(respuesta.texto);
    });
    }
   this.registrarse=function(){
        window.location = "index.html?root=registrarse"
        alert("Bienvenido");
  }
  this.recuperar = function(){
    sessionStorage.jugador=self.nombreDeUsuario();
   	window.location="index.html?root=recuperar"
   	//alert("recuperar");
}
this.acerca= function(){
  window.location="index.html?root=acercade"
}
}

    return new LoginViewModel();
  })
//)
;