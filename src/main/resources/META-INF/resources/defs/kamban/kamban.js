PrimeFaces.widget.Kamban = PrimeFaces.widget.DeferredWidget.extend({

    init: function (cfg) {
        this._super(cfg);
        var $this = this;
        //
        // console.log(cfg);
        // console.log(this.jq);

        this.columns = this.jq.children('.de-kamban-column');
        this.itens = this.jq.find('.de-kamban-item');

        this.columns.on('drop', function (e) {
            e.preventDefault();
            if (e.target.classList.contains('de-kamban-column')) {
                let itemDrop = document.getElementById(e.originalEvent.dataTransfer.getData("card-drop-id"));
                let column =  e.target;
                let item = $this.getElementInsertAfter(column, e)

                if (item != null) {
                    item.insertAdjacentElement("afterend", itemDrop);
                }else{
                    e.target.appendChild(itemDrop);
                }


                if ($this.hasBehavior('eventMove')) {
                    var ext = {
                        params: [
                            {name: 'column-drop-id', value: column.id},
                            {name: 'card-drop-id', value: e.originalEvent.dataTransfer.getData("card-drop-id")}
                        ]
                    };

                    $this.callBehavior('eventMove', ext);
                }
            }
        });

        this.columns.on('dragover', function (e) {
            e.preventDefault();
        });

        this.itens.on('dragstart', function (e) {
            e.originalEvent.dataTransfer.setData("card-drop-id", e.target.dataset.id);
            e.stopPropagation();
        });
    },

    getElementInsertAfter: function (coluna, event) {
        let listItems = coluna.querySelectorAll('li.de-kamban-item');

        for (let item of listItems) {
            let domRec = item.getBoundingClientRect();
            if (event.clientY > domRec.y) {
                return item;
            }
        }
        return null;
    }
});