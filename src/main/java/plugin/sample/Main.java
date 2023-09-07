package plugin.sample;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;


public final class Main extends JavaPlugin implements Listener {
    private int count;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

    }


    /**
     * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
     *
     * @param e イベント
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
        // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
        Player player = e.getPlayer();
        World world = player.getWorld();

        List<Color> colorList = List.of(Color.MAROON, Color.ORANGE, Color.WHITE, Color.YELLOW);
        if (count % 2 == 0) {
            for (Color color : colorList) {

                // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
                Firework firework = world.spawn(player.getLocation(), Firework.class);

                // 花火オブジェクトが持つメタ情報を取得。
                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                // メタ情報に対して設定を追加したり、値の上書きを行う。
                // 今回は青色で星型の花火を打ち上げる。
                fireworkMeta.addEffect(
                        FireworkEffect.builder()
                                .withColor(color)
                                .with(Type.BALL_LARGE)
                                .withFlicker()
                                .build());
                fireworkMeta.setPower(2);

                // 追加した情報で再設定する。
                firework.setFireworkMeta(fireworkMeta);
            }
            Path path = Path.of("firework.txt");
            Files.writeString(path, "たーまやー");
            player.sendMessage(Files.readString(path)); //例外処理するをクリック

        }
        count++;
    }


    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        Player player = e.getPlayer();
        ItemStack[] itemStacks = player.getInventory().getContents();
        for (ItemStack item : itemStacks) {
            if (!Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64) {
                item.setAmount(64);
            }
        }
        //プレイヤーの所持状況を上書き
        player.getInventory().setContents(itemStacks);
    }


    //ダメージアップさせる
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        double damage = e.getDamage();
        EntityType type = e.getEntityType();
        if (type != EntityType.PLAYER) {
            e.setDamage(damage * 10);
        }
        if (type == EntityType.PLAYER) {
            e.setCancelled(true);
        }
    }
}

//サンプルです。2回目です。







