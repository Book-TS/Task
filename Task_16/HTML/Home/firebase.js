// Them thu vien
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.18.0/firebase-app.js";
import { getDatabase, ref, set, child, get, onValue, update} from "https://www.gstatic.com/firebasejs/9.18.0/firebase-database.js";

// Cau hinh firebase
const firebaseConfig = {
    apiKey: "AIzaSyCpPpRkjFtYINVc2QJSimkbCtGIe5zQUrM",
    authDomain: "dieu-khien-nhiet-do-dixell.firebaseapp.com",
    databaseURL: "https://dieu-khien-nhiet-do-dixell-default-rtdb.firebaseio.com",
    projectId: "dieu-khien-nhiet-do-dixell",
    storageBucket: "dieu-khien-nhiet-do-dixell.appspot.com",
    messagingSenderId: "961902819526",
    appId: "1:961902819526:web:cf034f57e9acac8bfa7fc7",
    measurementId: "G-RWC55H8W3Z"
  };

// Khai bao bien  
const app = initializeApp(firebaseConfig);
const database = getDatabase(app);
const db = getDatabase();

var temp = 0;
var SW_1 = document.getElementById('SW_1');
var SW_2 = document.getElementById('SW_2');

const ref_temp = ref(db, 'Current/temp');
onValue(ref_temp, (snapshot) => {
    temp = snapshot.val();
    document.getElementById('temperature').innerHTML = temp + "°C";
});

const ref_sw1 = ref(db, 'Current/status');
onValue(ref_sw1, (snapshot) => {
    console.log(snapshot.val());
    if (snapshot.val()) {
        SW_1.checked = true;
    }
    else {
        SW_1.checked = false;
    }
});

const ref_sw2 = ref(db, 'Current/unit');
onValue(ref_sw2, (snapshot) => {
    console.log(snapshot.val());
    if (snapshot.val() == "°K") {
        SW_2.checked = true;
        document.getElementById('temperature').innerHTML = temp + 273 + "°K";
    }
    else {
        SW_2.checked = false;
        document.getElementById('temperature').innerHTML = temp + "°C";
    }
});

SW_1.addEventListener('change',function(){
    update(ref(db, 'Current'), { status: this.checked });
});

SW_2.addEventListener('change',function(){
    if(this.checked) {
        update(ref(db, 'Current'), { unit: "°K" });
    } else {
        update(ref(db, 'Current'), { unit: "°C" });
    }
});