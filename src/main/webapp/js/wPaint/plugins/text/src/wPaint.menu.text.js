(function ($) {

  // setup menu
  $.fn.wPaint.menus.text = {
    img: 'plugins/text/img/icons-menu-text.png',
    items: {
      bold: {
        icon: 'toggle',
        title: 'Bold',
        index: 0,
        callback: function (toggle) { this.setFontBold(toggle); }
      },
      italic: {
        icon: 'toggle',
        title: 'Italic',
        index: 1,
        callback: function (toggle) { this.setFontItalic(toggle); }
      },
      /*underline: {
        icon: 'toggle',
        title: 'Undelrine',
        index: 2,
        callback: function (toggle) { this.setFontUnderline(toggle); }
      },*/
      fontSize: {
        title: 'Font Size',
        icon: 'select',
        range: [8, 9, 10, 12, 14, 16, 20, 24, 30],
        value: 12,
        callback: function (size) { this.setFontSize(size); }
      },
      fontFamily: {
        icon: 'select',
        title: 'Font Family',
        range: ['Arial', 'Courier', 'Times', 'Verdana'],
        useRange: true,
        value: 'Arial',
        callback: function (family) { this.setFontFamily(family); }
      }
    }
  };

  // add icon to main menu
  $.fn.wPaint.menus.main.items.text = {
    icon: 'menu',
    after: 'pencil',
    title: 'Text',
    index: 7,
    callback: function () { this.setMode('text'); }
  };

  // extend defaults
  $.extend($.fn.wPaint.defaults, {
    fontSize       : '12',    // current font size for text input
    fontFamily     : 'Arial', // active font family for text input
    fontBold       : false,   // text input bold enable/disable
    fontItalic     : false,   // text input italic enable/disable
    fontUnderline  : false,    // text input italic enable/disable
    textColor       : $.fn.wPaint.defaults.strokeStyle, 
    textBorderColor : $.fn.wPaint.defaults.fillStyle,
    textBgColor     : $.fn.wPaint.defaults.fillStyle
  });

  // extend functions
  $.fn.wPaint.extend({
    generate: function () {
      this.$textCalc = $('<div></div>').hide();

      // make sure clicking on the text-tnput doesn't trigger another textInput
      this.$textInput = $('<textarea id="toto" class="wPaint-text-input" spellcheck="false"></textarea>')
      .on('mousedown', this._stopPropagation) 
      .css({position: 'absolute'})
      .hide();
      
      $('body').append(this.$textCalc);
      this.$el.append(this.$textInput);

      this.menus.all.text = this._createMenu('text');
    },

    _init: function () {
      var _this = this;

      function inputClick() {
        _this._drawTextIfNotEmpty();
        _this.$textInput.hide();
        _this.$canvasTemp.hide();
      }

      // in case we click on another element while typing - just auto set the text
      for (var i in this.menus.all) {
        this.menus.all[i].$menu
        .on('click', inputClick)
        .on('mousedown', this._stopPropagation);
      }

      // same idea here for clicking outside of the canvas area
      $(document).on('mousedown', inputClick);
    },

    /****************************************
     * setters
     ****************************************/
    setFillStyle: function (fillStyle) {
      this.$textInput.css('color', this.options.textColor);
      this.$textInput.css('border', "1px solid"+ this.options.textBorderColor);
      this.$textInput.css('background-color', this.options.textBgColor);
    },

    setFontSize: function (size) {
      this.options.fontSize = parseInt(size, 10);
      this._setFont({fontSize: size + 'px', lineHeight: size + 'px'});
      this.menus.all.text._setSelectValue('fontSize', size);
    },

    setFontFamily: function (family) {
      this.options.fontFamily = family;
      this._setFont({fontFamily: family});
      this.menus.all.text._setSelectValue('fontFamily', family);
    },

    setFontBold: function (bold) {
      this.options.fontBold = bold;
      this._setFont({fontWeight: (bold ? 'bold' : '')});
    },

    setFontItalic: function (italic) {
      this.options.fontItalic = italic;
      this._setFont({fontStyle: (italic ? 'italic' : '')});
    },

    setFontUnderline: function (underline) {
      this.options.fontUnderline = underline;
      this._setFont({fontWeight: (underline ? 'underline' : '')});
    },

    _setFont: function (css) {
      this.$textInput.css(css);
      this.$textCalc.css(css);
    },

    /****************************************
     * Text
     ****************************************/
    _drawTextDown: function (e) {
      this._drawTextIfNotEmpty();
      this._drawShapeDown(e, 1);

      this.$textInput
      .css({left: e.pageX - 1, top: e.pageY - 1, width: 0, height: 0})
      .show().focus();
    },
    
    _drawTextMove: function (e) {
      this._drawShapeMove(e, 1);

      this.$textInput.css({left: e.left - 1, top: e.top - 1, width: e.width, height: e.height});
    },

    _drawTextIfNotEmpty: function () {
      if (this.$textInput.val() !== '') { this._drawText(); }
    },

    // just draw text - don't want to trigger up here since we are just copying text from input box here
    _drawText: function () {
        var wi = $("#toto").width();
        var he = $("#toto").height();
      var fontString = '',
          lines = this.$textInput.val().split('\n'),
          linesNew = [],
          textInputWidth = this.$textInput.width() - 2,
          width = 0,
          lastj = 0,
          offset = this.$textInput.position(),
          left = offset.left + 1,
          top = offset.top + 1,
          //underlineOffset = 0,
          i, ii, j, jj;

      if (this.options.fontItalic) { fontString += 'italic '; }
      //if(this.settings.fontUnderline) { fontString += 'underline '; }
      if (this.options.fontBold) { fontString += 'bold '; }
      
      fontString += this.options.fontSize + 'px ' + this.options.fontFamily;
      
      for (i = 0, ii = lines.length; i < ii; i++) {
        this.$textCalc.html('');
        lastj = 0;
        
        for (j = 0, jj = lines[0].length; j < jj; j++) {
          width = this.$textCalc.append(lines[i][j]).width();
          
          if (width > textInputWidth) {
            linesNew.push(lines[i].substring(lastj, j));
            lastj = j;
            this.$textCalc.html(lines[i][j]);
          }
        }
        
        if (lastj !== j) { linesNew.push(lines[i].substring(lastj, j)); }
      }
      
      lines = this.$textInput.val(linesNew.join('\n')).val().split('\n');

        var width = 0;
        var height = 0;
        for (i = 0, ii = lines.length; i < ii; i++) {
          this.ctx.textBaseline = 'top';
          this.ctx.font = fontString;


          /// get width of text
          if(this.ctx.measureText(lines[i]).width > width) {
              width = this.ctx.measureText(lines[i]).width;
          }
          
          height += this.options.fontSize;
        }
        
        /// rect for border
        /// draw background rect assuming height of font
        this.ctx.fillStyle = this.options.textBorderColor;
        this.ctx.fillRect(parseInt(left), parseInt(top), parseInt(wi), parseInt(3));
        this.ctx.fillRect(parseInt(left), parseInt(top), parseInt(3), parseInt(he));
        this.ctx.fillRect(parseInt(left+wi), parseInt(top), parseInt(3), parseInt(he));
        this.ctx.fillRect(parseInt(left), parseInt(top+he), parseInt(wi+3), parseInt(3));
        this.ctx.fillRect(parseInt(left), parseInt(top), parseInt(width), parseInt(height));

        /// draw background rect assuming height of font
        this.ctx.fillStyle = this.options.textBgColor;
        this.ctx.fillRect(parseInt(left+2), parseInt(top+2), parseInt(wi-4), parseInt(he-4));
        /// color for text
        this.ctx.fillStyle = this.options.textColor;

      for (i = 0, ii = lines.length; i < ii; i++) {
        this.ctx.textBaseline = 'top';
        this.ctx.font = fontString;
        this.ctx.fillText(lines[i], left, top);
        
        top += this.options.fontSize;
        
        /*if(lines[i] !== '' && this.options.fontTypeUnderline) {
          width = this.$textCalc.html(lines[i]).width();
          
          //manually set pixels for underline since to avoid antialiasing 1px issue, and lack of support for underline in canvas
          var imgData = this.ctx.getImageData(0, top+underlineOffset, width, 1);
          
          for (j=0; j<imgData.width*imgData.height*4; j+=4) {
            imgData.data[j] = parseInt(this.options.fillStyle.substring(1,3), 16);
            imgData.data[j+1] = parseInt(this.options.fillStyle.substring(3,5), 16);
            imgData.data[j+2] = parseInt(this.options.fillStyle.substring(5,7), 16);
            imgData.data[j+3] = 255;
          }
          
          this.ctx.putImageData(imgData, left, top+underlineOffset);
        }*/
      }
      
      saveDatamapEntry($("#toto").val());

      this.$textInput.val('');
      this._addUndo();
    }
  });
})(jQuery);
