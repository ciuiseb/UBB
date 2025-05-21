document.addEventListener('DOMContentLoaded', function () {
    const list1 = document.getElementById('list1');
    const list2 = document.getElementById('list2');

    function moveItem(sourceList, targetList) {
        const currentItem = sourceList.options[sourceList.selectedIndex];

        if (currentItem) {
            const item = document.createElement('option');
            item.value = currentItem.value;
            item.text = currentItem.text;//item="<option > </option>"

            targetList.add(item);
            sourceList.remove(sourceList.selectedIndex);
        }
    }

    list1.addEventListener('dblclick', function () {
        moveItem(list1, list2);
    });

    list2.addEventListener('dblclick', function () {
        moveItem(list2, list1);
    });
});