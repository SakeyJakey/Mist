mod = script.registerModule('BHop', 'Movement');
alwaysJump = mod.addBoolSetting('Always jump', false);
mod.onEvent('motion', function(e)
{
    if(player.isOnGround() && (player.isMoving() || mod.getBoolSetting(alwaysJump)))
        player.jump();
});