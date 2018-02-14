/**
 * Copyright (c) 2014, 2017, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
/*
 * Your application specific code will go here
 */
define(['ojs/ojcore', 'ojs/ojrouter', 'ojs/ojarraytabledatasource', 'ojs/ojoffcanvas', 'ojs/ojbutton'],
  function(oj) {
     function ControllerViewModel() {
      var self = this;

      // Router setup
      self.router = oj.Router.rootInstance;
      self.router.configure({
       'login': {label: 'Login', isDefault: true},
       'estadisticas': {label: 'Estadisticas'},
       'ter': {label: 'Tres En Raya'},
       'hf': {label: 'Hundir La Flota'},
       'miCuenta': {label: 'Mi Cuenta'},
       'acercade': {label: 'Acerca de'},
       'registrarse': {label: 'Registrarse'},
       'recuperar': {label: 'Recuperar'},
       'ranking' : {label: 'Ranking'}
      });
      oj.Router.defaults['urlAdapter'] = new oj.Router.urlParamAdapter();
      self.moduleConfig = self.router.moduleConfig;

      // Navigation setup
      var navData = [
      {name: 'Login', id: 'login',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-chart-icon-24'},
      {name: 'Estadisticas', id: 'estadisticas',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-fire-icon-24'},
      {name: 'Tres En Raya', id: 'ter',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-people-icon-24'},
       {name: 'Hundir La Flota', id: 'hf',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-people-icon-24'},
      {name: 'Mi Cuenta', id: 'miCuenta',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-person-icon-24'},
      {name: 'Acerca de', id: 'acercade',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-info-icon-24'},
       {name: 'Registrarse', id: 'registrarse',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-info-icon-24'},
       {name: 'Recuperar', id: 'recuperar',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-info-icon-24'},
       {name: 'Ranking', id: 'ranking',
       iconClass: 'oj-navigationlist-item-icon demo-icon-font-24 demo-info-icon-24'}
      ];
      self.navDataSource = new oj.ArrayTableDataSource(navData, {idAttribute: 'id'});

      // Drawer setup
      self.toggleDrawer = function() {
        return oj.OffcanvasUtils.toggle({selector: '#navDrawer', modality: 'modal', content: '#pageContent'});
      }
      // Add a close listener so we can move focus back to the toggle button when the drawer closes
      $("#navDrawer").on("ojclose", function() { $('#drawerToggleButton').focus(); });

      // Header Setup
      self.getHeaderModel = function() {
        var headerFactory = {
          createViewModel: function(params, valueAccessor) {
            var model =  {
              pageTitle: self.router.currentState().label,
              handleBindingsApplied: function(info) {
                // Adjust content padding after header bindings have been applied
                self.adjustContentPadding();
              },
              toggleDrawer: self.toggleDrawer
            };
            return Promise.resolve(model);
          }
        }
        return headerFactory;
      }

      // Method for adjusting the content area top/bottom paddings to avoid overlap with any fixed regions. 
      // This method should be called whenever your fixed region height may change.  The application
      // can also adjust content paddings with css classes if the fixed region height is not changing between 
      // views.
      self.adjustContentPadding = function () {
        var topElem = document.getElementsByClassName('oj-applayout-fixed-top')[0];
        var contentElem = document.getElementsByClassName('oj-applayout-content')[0];
        var bottomElem = document.getElementsByClassName('oj-applayout-fixed-bottom')[0];

        if (topElem) {
          contentElem.style.paddingTop = topElem.offsetHeight+'px';
        }
        if (bottomElem) {
          contentElem.style.paddingBottom = bottomElem.offsetHeight+'px';
        }
        // Add oj-complete marker class to signal that the content area can be unhidden.
        // See the override.css file to see when the content area is hidden.
        contentElem.classList.add('oj-complete');
      }
    }

    return new ControllerViewModel();
  }
);
