package io.github.yanestyl.example.content;

import io.github.yanestyl.jgram.annotation.BotController;
import io.github.yanestyl.jgram.annotation.content.OnAnimation;
import io.github.yanestyl.jgram.annotation.content.OnAudio;
import io.github.yanestyl.jgram.annotation.content.OnCommand;
import io.github.yanestyl.jgram.annotation.content.OnContact;
import io.github.yanestyl.jgram.annotation.content.OnDocument;
import io.github.yanestyl.jgram.annotation.content.OnLocation;
import io.github.yanestyl.jgram.annotation.content.OnMessage;
import io.github.yanestyl.jgram.annotation.content.OnPhoto;
import io.github.yanestyl.jgram.annotation.content.OnSticker;
import io.github.yanestyl.jgram.annotation.content.OnVideo;
import io.github.yanestyl.jgram.annotation.content.OnVoice;
import io.github.yanestyl.jgram.context.AnimationContext;
import io.github.yanestyl.jgram.context.AudioContext;
import io.github.yanestyl.jgram.context.ContactContext;
import io.github.yanestyl.jgram.context.DocumentContext;
import io.github.yanestyl.jgram.context.LocationContext;
import io.github.yanestyl.jgram.context.MessageContext;
import io.github.yanestyl.jgram.context.PhotoContext;
import io.github.yanestyl.jgram.context.StickerContext;
import io.github.yanestyl.jgram.context.VideoContext;
import io.github.yanestyl.jgram.context.VoiceContext;

@BotController
public class ContentBotController {

    // ---------------------------------------------------------------
    // Старт — multiple commands
    // ---------------------------------------------------------------

    @OnCommand({"/start", "/help", "/h"})
    public String start(MessageContext ctx) {
        return "Привет, " + ctx.user().firstName() + "! 👋\n\n" +
                "Отправь мне любой тип контента:\n" +
                "🖼 Фото\n" +
                "🎬 Видео\n" +
                "📄 Документ\n" +
                "🎵 Аудио\n" +
                "🎤 Голосовое\n" +
                "😀 Стикер\n" +
                "🎭 GIF (анимация)\n" +
                "📞 Контакт\n" +
                "📍 Геолокация";
    }

    // ---------------------------------------------------------------
    // Фото
    // ---------------------------------------------------------------

    @OnPhoto
    public void photo(PhotoContext ctx) {
        ctx.reply("🖼 Фото получено!\n" +
                "File ID: " + ctx.fileId() + "\n" +
                (ctx.caption().isEmpty() ? "" : "Подпись: " + ctx.caption()));
    }

    // ---------------------------------------------------------------
    // Видео
    // ---------------------------------------------------------------

    @OnVideo
    public void video(VideoContext ctx) {
        ctx.reply("🎬 Видео получено!\n" +
                "Длительность: " + ctx.duration() + " сек\n" +
                "Размер: " + ctx.width() + "x" + ctx.height() + "\n" +
                "File size: " + ctx.fileSize() + " байт\n" +
                (ctx.caption().isEmpty() ? "" : "Подпись: " + ctx.caption()));
    }

    // ---------------------------------------------------------------
    // Документ
    // ---------------------------------------------------------------

    @OnDocument
    public void document(DocumentContext ctx) {
        ctx.reply("📄 Документ получен!\n" +
                "Имя файла: " + ctx.fileName() + "\n" +
                "Тип: " + ctx.mimeType() + "\n" +
                "Размер: " + ctx.fileSize() + " байт\n" +
                (ctx.caption().isEmpty() ? "" : "Подпись: " + ctx.caption()));
    }

    // ---------------------------------------------------------------
    // Аудио
    // ---------------------------------------------------------------

    @OnAudio
    public void audio(AudioContext ctx) {
        ctx.reply("🎵 Аудио получено!\n" +
                "Название: " + (ctx.title().isEmpty() ? "неизвестно" : ctx.title()) + "\n" +
                "Исполнитель: " + (ctx.performer().isEmpty() ? "неизвестно" : ctx.performer()) + "\n" +
                "Длительность: " + ctx.duration() + " сек");
    }

    // ---------------------------------------------------------------
    // Голосовое
    // ---------------------------------------------------------------

    @OnVoice
    public void voice(VoiceContext ctx) {
        ctx.reply("🎤 Голосовое сообщение получено!\n" +
                "Длительность: " + ctx.duration() + " сек\n" +
                "Тип: " + ctx.mimeType());
    }

    // ---------------------------------------------------------------
    // Стикер
    // ---------------------------------------------------------------

    @OnSticker
    public void sticker(StickerContext ctx) {
        String type = ctx.isVideo() ? "видео-стикер" :
                ctx.isAnimated() ? "анимированный" : "обычный";

        ctx.reply("😀 Стикер получен!\n" +
                "Тип: " + type + "\n" +
                "Эмодзи: " + (ctx.emoji() != null ? ctx.emoji() : "нет") + "\n" +
                "Набор: " + (ctx.setName() != null ? ctx.setName() : "нет"));
    }

    // ---------------------------------------------------------------
    // Анимация (GIF)
    // ---------------------------------------------------------------

    @OnAnimation
    public void animation(AnimationContext ctx) {
        ctx.reply("🎭 GIF получен!\n" +
                "Файл: " + ctx.fileName() + "\n" +
                "Длительность: " + ctx.duration() + " сек\n" +
                "Размер: " + ctx.width() + "x" + ctx.height() + "\n" +
                (ctx.caption().isEmpty() ? "" : "Подпись: " + ctx.caption()));
    }

    // ---------------------------------------------------------------
    // Контакт
    // ---------------------------------------------------------------

    @OnContact
    public void contact(ContactContext ctx) {
        ctx.reply("📞 Контакт получен!\n" +
                "Имя: " + ctx.firstName() +
                (ctx.lastName() != null ? " " + ctx.lastName() : "") + "\n" +
                "Телефон: " + ctx.phoneNumber() + "\n" +
                (ctx.contactUserId() != null ?
                        "Telegram ID: " + ctx.contactUserId() : "Не в Telegram"));
    }

    // ---------------------------------------------------------------
    // Геолокация
    // ---------------------------------------------------------------

    @OnLocation
    public void location(LocationContext ctx) {
        ctx.reply("📍 Геолокация получена!\n" +
                "Широта: " + ctx.latitude() + "\n" +
                "Долгота: " + ctx.longitude() + "\n" +
                "Google Maps: https://maps.google.com/?q=" +
                ctx.latitude() + "," + ctx.longitude());
    }

    // ---------------------------------------------------------------
    // Всё остальное
    // ---------------------------------------------------------------

    @OnMessage
    public String unknown() {
        return "Не понимаю этот тип сообщения 🤔\n" +
                "Отправь /start чтобы увидеть что я умею!";
    }
}
