# RyuZUCommandsGenerator
マイクラのコマンド実装がだるすぎたので作ったよ！
## 使い方
### まず初めに
onEnableにRyuZUCommandsGeneratorのインスタンスを作成！(後に記述するCommandの登録を行ったあとの行でインスタンスを作成してください)
インスタンスの作成と同時に、CommandsGeneratorで登録したコマンドがすべてサーバーに登録されます。
```
new RyuZUCommandsGenerator(JavaPlugin);
```
または
```
new RyuZUCommandsGenerator(JavaPlugin, String);
```
第二引数のStrginはコマンドを入力する権限がなかった時表示されるメッセージ
Default: ChatColor.RED + "権限がありません"
### コマンドを登録しよう
```
CommandsGenerator.registerCommand(String, Consumer<CommandData>)
CommandsGenerator.registerCommand(String, Consumer<CommandData>, String)
CommandsGenerator.registerCommand(String, Consumer<CommandData>, List<String>)
CommandsGenerator.registerCommand(String, Consumer<CommandData>, String, Predicate<CommandData>, Predicate<CommandData>)
CommandsGenerator.registerCommand(String, Consumer<CommandData>, List<String>, Predicate<CommandData>)
CommandsGenerator.registerCommand(String, Consumer<CommandData>, String, Predicate<CommandData>, Predicate<CommandData>)
CommandsGenerator.registerCommand(String, Consumer<CommandData>, List<String>, Predicate<CommandData>, Predicate<CommandData>)

registerCommandメソッドではCommandCompositionが返り値として与えられるので、CommandCompositionに対してBuilderのようにconditionやpermissionsを書き連ねることも可能です。
```
- 第一引数: コマンド
- 第二引数: 実行する内容
- 第三引数: 必要なパーミッション
- 第四引数: 実行するための条件
- 第五引数: タブ保管するための条件

例1:
```
        CommandsGenerator.registerCommand("test",
                data -> {
                    if (data.getSender().hasPermission("test.op")) {
                        data.sendMessage(ChatColor.RED + "/" + data.getLabel() + " [list/add/remove]");
                    } else {
                        data.sendMessage(ChatColor.RED + "/" + data.getLabel() + " [list]");
                    }
                },
                "test.player",
                data -> data.getArgs().length == 0
        );
```

例2:
```
            CommandsGenerator.registerCommand("test.get",
                    data -> {
                        Player p = (Player) data.getSender();
                        double amount = Double.parseDouble(data.getArgs()[1]);
                        data.sendMessage(ChatColor.GREEN + "ダイヤモンドを与えました");
                        ItemStack diamond = new ItemStack(Material.DIAMOND);
                        diamond.setAmount(amount);
                        p.getInventory().addItem(diamond);
                    },
                    "test.op",
                    data -> {
                        if (data.getArgs().length <= 1) {
                            data.sendMessage(ChatColor.RED + "/" + data.getLabel() + " test <amount>");
                            return false;
                        }

                        try {
                            Double.parseDouble(data.getArgs()[1]);
                        } catch (NumberFormatException e) {
                            data.sendMessage(ChatColor.RED + "自然数を入力してください");
                            return false;
                        }
                        return true;
                    },
                    data -> {
                        if (!(data.getSender() instanceof Player)) {
                            data.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
                            return false;
                        }
                        return true;
                    }
            );
```

例3:
```
        CommandsGenerator.registerCommand("test.entry",
                data -> {
                    Player p = (Player) data.getSender();
                    ~~~
                    ゲームにエントリーするコード
                    ~~~
                    data.getSender().sendMessage(ChatColor.GREEN + "ゲームにエントリーしました");
                },
                "test.op",
                data -> true,
                data -> {
                    if (!(data.getSender() instanceof Player)) {
                        data.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
                        return false;
                    }
                    return true;
                }
        );
```
タブ保管を後述で追記する方法もあります。
complete(index , List<String>)で指定した引数目にタブ保管を追加します。
completePlayer(index)はすべてのオンラインプレイヤーを指定した引数目に保管として追加します。

#### 注意点
第五引数(タブ保管するための条件)でメッセージを送るときは
```
data#.sendMessage(String);
```
を使用してください。
Playerから直接送信するとタブ保管するたびにメッセージが送られます。

コマンドは第三引数(パーミッション) -> 第五引数(タブ保管条件)-> 第四引数(コマンド実行条件)
の順に条件チェックを行い。すべての条件がかみ合っていた場合のみ実行されます(そのため、上記の例3では第五引数ですでにプレイヤーチェックを済ませているため、第四引数では常にtrueを返しています) 

#### plugin.ymlは自分で書く
上記の例の場合はtestについて書く
