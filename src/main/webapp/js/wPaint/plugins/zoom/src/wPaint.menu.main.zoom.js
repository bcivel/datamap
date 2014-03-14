(function ($) {
  var img = 'plugins/zoom/img/icons-menu-main-zoom.png';

  // extend menu
  $.extend(true, $.fn.wPaint.menus.main.items, {
    zoom: {
      icon: 'generic',
      title: 'zoomImg',
      img: img,
      index: 0,
      callback: function () {
        this.options.zoomImgBg.apply(this, [this.getImage()]);
      }
    }
  });

  // extend defaults
  $.extend($.fn.wPaint.defaults, {
    zoomImgBg: null   // callback triggerd on image zoom
  });

  // extend functions
  $.fn.wPaint.extend({
    _zoomImgBg: function (type, images) {
        var _this = this,
        $content = $('<div></div>'),
        $img = null;

        //alert("test");
        this.ctx.scale(2,2);
        this.ctx.redraw();
        /*
        var bg = this.getBg(),
            image = this.getImage();

        this.width = this.$el.width()*2;
        this.height = this.$el.height()*2;

        this.canvasBg.width = this.width;
        this.canvasBg.height = this.height;
        this.canvas.width = this.width;
        this.canvas.height = this.height;

        this.ctxBgResize = false;
        this.setBg(bg, true);
        this.ctxResize = false;
        this.setImage(image, '', true, true);
        */
    }
  });
})(jQuery);