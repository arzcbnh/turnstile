# Turnstile

Turnstile is a server-side Fabric mod that adds token-based authentication to offline-mode Minecraft servers. Players authenticate by connecting through a server address with a personal token as its first subdomain, so no in-game login command is required.

> Turnstile is intended for offline-mode servers. It does not replace Mojang/Microsoft account authentication and is not needed on servers with online mode enabled.

## Requirements

- Fabric Loader `0.19.3` or compatible
- Fabric API `0.153.0+26.2` or compatible

Configure a wildcard DNS record such as `*.mc.example.com` pointing to your server or proxy. This mod is not suitable for servers without a domain name.

## How it works

1. A player connects using their personal token as the bottom-level domain:

```text
TOKEN.mc.example.com
```

2. Turnstile verifies the token against the player's offline-mode UUID.
3. If valid, the player may join. The token remains valid until it is regenerated or revoked.

### Token generation

A connected client must request a token by executing the command `/turnstile token generate` or interacting with prompts.

For security reasons, a server operator cannot generate a token for another player. Because of this, a one-time pass must be granted to players before a token request can be made. The token is not logged on the server and is hashed before being stored.

Generating a new token replaces the previous one for that player.

### One-time passes

Server moderators can grant one-time passes to let a player join without their token. After joining with a pass, the player is prompted to generate a new token. When a player successfully authenticates with their token, their passes are reset.

## Commands

| Command                                   | Permission | Description                                                        |
|-------------------------------------------|------------|--------------------------------------------------------------------|
| `/turnstile token generate`               | Any player | Generates a new token for yourself.                                |
| `/turnstile token revoke <player>`        | Moderator  | Revokes a player's token.                                          |
| `/turnstile passes get`                   | Any player | Shows your remaining one-time passes.                              |
| `/turnstile passes get <player>`          | Moderator  | Shows a player's remaining one-time passes.                        |
| `/turnstile passes set <player> <amount>` | Moderator  | Sets a player's one-time pass count. Use `0` to remove all passes. |

## Configuration

### Server domain

When providing a token to a player, the server has the option of returning a full address instead of just the token. For that, the server domain must be written in `config/turnstile.txt`. If the server wishes to display just the token, the file should not exist.

For example, a file with the contents `mc.example.com` would display `TOKEN.mc.example.com` to a player.

### Game rule

Authentication can be enabled or disabled at runtime:

```mcfunction
/gamerule turnstile:enable true
```

It is enabled by default. Disabling it restores the server's usual offline-mode login behavior.

## Security concerns

Since tokens are exposed in the hostname, they are visible to DNS providers and proxies, and may be recorded in logs. Use a software stack that you have control over, and only share server infrastructure with people you trust. Always keep backups of your server.

The mod is currently in beta and may contain other vulnerabilities. Other approaches to authentication, security features, and changes to the interface are expected to be introduced in future versions.

## License

Copyright (C) 2026 arzcbnh

This project is licensed under the [GNU General Public License v3.0 only](LICENSE.txt).
