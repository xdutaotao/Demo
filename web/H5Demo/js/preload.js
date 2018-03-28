(function($) {

    function PreLoad(imgs, options) {
        this.imgs = (typeof imgs === 'string') ? [imgs] : imgs;
        this.opts = $.extend({}, PreLoad.DEFAULTS, options);

        this._unordered();
    }

    PreLoad.DEFAULTS = {
        each: null,
        all: null
    }

    PreLoad.prototype._unordered = function() {
        var opts = this.opts;
        var count = 0;
        $.each(this.imgs, function(i, src) {
            if (typeof src != 'string')
                return;

            var img = new Image();
            $(img).on('load error', function() {
                opts.each && opts.each(count);

                if (count + 1 >= len) {
                    opts.all && opts.all();
                }
                count++;
            })
            img.src = src;
        })
    }

    $.extend({
        preload: function(imgs, opts) {
            new PreLoad(imgs, opts);
        }
    })

})(jQuery);