$(document).ready(function () {
    const list1 = $('#list1');
    const list2 = $('#list2');

    function moveItem(sourceList, targetList) {
        const currentItem = sourceList.find('option:selected');

        if (currentItem) {
            targetList.append(
                $('<option></option>')
                    .val(currentItem.val())
                    .text(currentItem.text())
            )
            currentItem.remove();
        }
    }

    list1.on('dblclick', function () {
        moveItem(list1, list2);
    });
    list2.on('dblclick', function () {
        moveItem(list2, list1)
    });
});
