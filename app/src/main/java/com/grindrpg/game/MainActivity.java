package com.grindrpg.game;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView web = new WebView(this);
        web.setWebViewClient(new WebViewClient());

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        String html =
            "<!DOCTYPE html>" +
            "<html><head><meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
            "<title>Grind RPG</title>" +

            "<style>" +
            "body{background:#15151f;color:white;font-family:sans-serif;text-align:center;margin:0;padding:12px}" +
            "h1{color:#a78bfa}" +
            ".box{background:#252538;border-radius:12px;padding:12px;margin:10px 0}" +
            "button{background:#7655d9;color:white;border:0;border-radius:9px;padding:12px;margin:4px;font-size:14px}" +
            ".monster{background:#35354b}" +
            ".bar{background:#444;height:18px;border-radius:10px;overflow:hidden}" +
            ".hp{background:#ef4444;height:100%;width:100%}" +
            ".xp{background:#22c55e;height:100%;width:0%}" +
            ".small{font-size:12px;color:#c7c7d9}" +
            "</style></head><body>" +

            "<h1>⚔ Grind RPG</h1>" +

            "<div class='box'>" +
            "<h2 id='monsterName'></h2>" +
            "<p>HP: <span id='hpText'></span></p>" +
            "<div class='bar'><div id='hpBar' class='hp'></div></div>" +
            "<p id='monsterInfo'></p>" +
            "<button onclick='attack()'>⚔ 일반 공격</button>" +
            "<button onclick='autoToggle()' id='autoBtn'>자동 공격: OFF</button>" +
            "</div>" +

            "<div class='box'>" +
            "<h3>📊 플레이어 정보</h3>" +
            "<p>레벨: <b id='lv'>1</b></p>" +
            "<p>경험치: <span id='xp'>0</span> / <span id='xpNeed'>10</span></p>" +
            "<div class='bar'><div id='xpBar' class='xp'></div></div>" +
            "<p>일반 공격력: <b id='atk'></b></p>" +
            "<p>자동 공격력: <b id='autoAtk'></b></p>" +
            "<p>골드: 🪙 <b id='gold'>0</b></p>" +
            "</div>" +

            "<div class='box'>" +
            "<h3>⬆️ 업그레이드</h3>" +

            "<button onclick='upgradeAttack()'>일반 공격력 강화</button>" +
            "<p class='small' id='attackCost'></p>" +

            "<button onclick='upgradeGold()'>골드 획득량 강화</button>" +
            "<p class='small' id='goldCost'></p>" +

            "<button onclick='upgradeAutoDamage()'>자동 공격력 강화</button>" +
            "<p class='small' id='autoCost'></p>" +

            "<button onclick='upgradeSpeed()'>자동 공격 속도 강화</button>" +
            "<p class='small' id='speedInfo'></p>" +
            "</div>" +

            "<div class='box'>" +
            "<h3>👾 몬스터 선택</h3>" +
            "<div id='monsterList'></div>" +
            "</div>" +

            "<p id='message'>몬스터를 처치하고 성장하자!</p>" +

            "<script>" +

            "const monsters=[" +
            "{name:'슬라임',hp:10,xp:5,gold:3}," +
            "{name:'달팽이',hp:50,xp:15,gold:10}," +
            "{name:'고블린',hp:150,xp:40,gold:25}," +
            "{name:'박쥐',hp:400,xp:100,gold:60}," +
            "{name:'해골 전사',hp:1000,xp:250,gold:150}," +
            "{name:'오크',hp:3000,xp:600,gold:400}," +
            "{name:'골렘',hp:10000,xp:1500,gold:1000}," +
            "{name:'드래곤',hp:50000,xp:5000,gold:3500}," +
            "{name:'마왕',hp:250000,xp:20000,gold:15000}," +
            "{name:'최종 보스',hp:1000000,xp:100000,gold:100000}" +
            "];" +

            "const speeds=[1,0.5,0.3,0.2,0.15,0.1,0.08,0.05,0.03,0.02,0.01];" +

            "let lv=1,xp=0,gold=0;" +
            "let baseAtk=1,attackLevel=0;" +
            "let goldLevel=0,autoDamageLevel=0,speedLevel=0;" +
            "let autoOn=false,autoTimer=null;" +
            "let monsterIndex=0;" +
            "let monsterHP=monsters[0].hp;" +

            // 일반 공격력에는 baseAtk만 적용
            "function levelMultiplier(){return Math.pow(1.1,lv-1);}" +
            "function playerAttack(){return Math.max(1,Math.floor(baseAtk*levelMultiplier()));}" +

            // 자동 공격은 별도 기본 공격력 + 자동 업그레이드 보너스
            "function autoAttackPower(){" +
            "return Math.max(1,Math.floor(levelMultiplier())+autoDamageLevel*5);" +
            "}" +

            "function xpNeedValue(){return Math.floor(10*Math.pow(1.5,lv-1));}" +
            "function attackCost(){return Math.floor(20*Math.pow(1.65,attackLevel));}" +
            "function goldUpgradeCost(){return Math.floor(30*Math.pow(1.8,goldLevel));}" +
            "function autoDamageCost(){return Math.floor(25*Math.pow(1.7,autoDamageLevel));}" +
            "function speedCost(){return Math.floor(50*Math.pow(2,speedLevel));}" +

            "function attack(){damageMonster(playerAttack());}" +
            "function autoAttackHit(){damageMonster(autoAttackPower());}" +

            "function damageMonster(dmg){" +
            "monsterHP-=dmg;" +
            "if(monsterHP<=0){" +
            "let m=monsters[monsterIndex];" +
            "let earned=Math.floor(m.gold*Math.pow(1.5,goldLevel));" +
            "gold+=earned;" +
            "xp+=m.xp;" +
            "while(xp>=xpNeedValue()){" +
            "xp-=xpNeedValue();" +
            "lv++;" +
            "document.getElementById('message').innerText='레벨 업! 현재 레벨 '+lv;" +
            "}" +
            "monsterHP=m.hp;" +
            "document.getElementById('message').innerText='처치! +'+earned+' 골드, +'+m.xp+' XP';" +
            "}" +
            "render();" +
            "}" +

            "function upgradeAttack(){" +
            "let c=attackCost();" +
            "if(gold<c){msg('골드가 부족해!');return;}" +
            "gold-=c;" +
            "attackLevel++;" +
            "baseAtk++;" +
            "msg('일반 공격력 강화 완료!');" +
            "render();" +
            "}" +

            "function upgradeGold(){" +
            "let c=goldUpgradeCost();" +
            "if(gold<c){msg('골드가 부족해!');return;}" +
            "gold-=c;" +
            "goldLevel++;" +
            "msg('골드 획득량 증가!');" +
            "render();" +
            "}" +

            "function upgradeAutoDamage(){" +
            "let c=autoDamageCost();" +
            "if(gold<c){msg('골드가 부족해!');return;}" +
            "gold-=c;" +
            "autoDamageLevel++;" +
            "msg('자동 공격력 강화 완료!');" +
            "render();" +
            "}" +

            "function upgradeSpeed(){" +
            "if(speedLevel>=speeds.length-1){msg('최고 속도야!');return;}" +
            "let c=speedCost();" +
            "if(gold<c){msg('골드가 부족해!');return;}" +
            "gold-=c;" +
            "speedLevel++;" +
            "if(autoOn)startAuto();" +
            "msg('자동 공격 속도 증가!');" +
            "render();" +
            "}" +

            "function autoToggle(){" +
            "autoOn=!autoOn;" +
            "if(autoOn)startAuto();else stopAuto();" +
            "render();" +
            "}" +

            "function startAuto(){" +
            "stopAuto();" +
            "autoTimer=setInterval(autoAttackHit,Math.max(10,speeds[speedLevel]*1000));" +
            "}" +

            "function stopAuto(){" +
            "if(autoTimer!==null)clearInterval(autoTimer);" +
            "autoTimer=null;" +
            "}" +

            "function selectMonster(i){" +
            "monsterIndex=i;" +
            "monsterHP=monsters[i].hp;" +
            "msg(monsters[i].name+' 선택!');" +
            "render();" +
            "}" +

            "function msg(t){document.getElementById('message').innerText=t;}" +

            "function render(){" +
            "let m=monsters[monsterIndex];" +
            "document.getElementById('monsterName').innerText=m.name;" +
            "document.getElementById('hpText').innerText=Math.max(0,monsterHP).toLocaleString()+' / '+m.hp.toLocaleString();" +
            "document.getElementById('hpBar').style.width=Math.max(0,monsterHP/m.hp*100)+'%';" +
            "document.getElementById('monsterInfo').innerText='보상: '+m.xp+' XP / '+Math.floor(m.gold*Math.pow(1.5,goldLevel))+' 골드';" +
            "document.getElementById('lv').innerText=lv;" +
            "document.getElementById('xp').innerText=xp.toLocaleString();" +
            "document.getElementById('xpNeed').innerText=xpNeedValue().toLocaleString();" +
            "document.getElementById('xpBar').style.width=(xp/xpNeedValue()*100)+'%';" +
            "document.getElementById('atk').innerText=playerAttack().toLocaleString();" +
            "document.getElementById('autoAtk').innerText=autoAttackPower().toLocaleString();" +
            "document.getElementById('gold').innerText=gold.toLocaleString();" +
            "document.getElementById('attackCost').innerText='비용: '+attackCost().toLocaleString()+' 골드 | 강화: '+attackLevel+'회';" +
            "document.getElementById('goldCost').innerText='비용: '+goldUpgradeCost().toLocaleString()+' 골드 | 보상 배율: x'+Math.pow(1.5,goldLevel).toFixed(2);" +
            "document.getElementById('autoCost').innerText='비용: '+autoDamageCost().toLocaleString()+' 골드 | 강화 보너스: +'+(autoDamageLevel*5);" +
            "document.getElementById('speedInfo').innerText='현재 간격: '+speeds[speedLevel]+'초 | 다음 비용: '+(speedLevel<speeds.length-1?speedCost().toLocaleString()+' 골드':'최고 속도');" +
            "document.getElementById('autoBtn').innerText='자동 공격: '+(autoOn?'ON':'OFF');" +
            "}" +

            "let list='';" +
            "for(let i=0;i<monsters.length;i++){" +
            "list+='<button class=\"monster\" onclick=\"selectMonster('+i+')\">'+monsters[i].name+'<br><span class=\"small\">HP '+monsters[i].hp.toLocaleString()+'</span></button>';" +
            "}" +
            "document.getElementById('monsterList').innerHTML=list;" +
            "render();" +

            "</script></body></html>";

        web.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        setContentView(web);
    }
}
