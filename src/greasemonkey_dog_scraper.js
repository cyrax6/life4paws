// ==UserScript==
// @name        ishelter
// @namespace   http://ishelter.ishelters.com/as/
// @description screen grab for all dog breeds
// @include     http://ishelter.ishelters.com/as/add.php
// @version     1
// @grant       none
// ==/UserScript==

var dog_breeds_sel = document.evaluate("//select[@class='req' and @name='b']/option ", document, null, XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE, null);
var num = dog_breeds_sel.snapshotLength;

var breed_ids = []
var breed_names = []
for (var i =0; i < num; i++)
{
    var it = dog_breeds_sel.snapshotItem(i);
    var breed_id = it.getAttribute("value");
    var breed_name = it.innerHTML;
    breed_ids.push(breed_id);
    breed_names.push(breed_name);
}

var buf_len = 10;
for(var i=0; i < breed_names.length; i = i+ buf_len)
{
    var str = "";

    for(var j=0; j < buf_len; j++)
    {
        var breed_name = breed_names[i+j];
        if(breed_name != undefined)
        {
            str = str + " , \"" + breed_names[i+j] + "\"";
        }
    }
    console.log(str);
}
