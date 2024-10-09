package com.github.bccyben.common.message;

/**
 * メッセージID定数
 */
public class MessageIdConstants {

    /**
     * リクエストが不正です。最初からやり直してください。
     */
    public static final String MESSAGE_E0001 = "E0001";
    /**
     * {0}が選択されてません。
     */
    public static final String MESSAGE_E0002 = "E0002";
    /**
     * {0}が入力されてません。
     */
    public static final String MESSAGE_E0003 = "E0003";
    /**
     * {0}が見つかりませんでした。
     */
    public static final String MESSAGE_E0004 = "E0004";
    /**
     * {0}が{1}マスターに存在しません。
     */
    public static final String MESSAGE_E0005 = "E0005";
    /**
     * {0}{1}はすでに使われてます。別{0}をご指定ください。
     */
    public static final String MESSAGE_E0006 = "E0006";
    /**
     * {0}在庫がありません。
     */
    public static final String MESSAGE_E0007 = "E0007";
    /**
     * 別のユーザーの操作により、{0}の在庫数量が不足しました。
     */
    public static final String MESSAGE_E0008 = "E0008";
    /**
     * 別のユーザーの操作により、{0}できません。
     */
    public static final String MESSAGE_E0009 = "E0009";
    /**
     * 製品情報、材料情報、副産物・廃棄物情報について、最低1つ以上のロット情報を入力する必要があります。
     */
    public static final String MESSAGE_E0010 = "E0010";
    /**
     * 材料及び廃棄物副産物の入力はできません。
     */
    public static final String MESSAGE_E0011 = "E0011";
    public static final String MESSAGE_E0012 = "E0012";
    public static final String MESSAGE_E0013 = "E0013";
    public static final String MESSAGE_E0014 = "E0014";
    public static final String CANNOT_DELETE = "cannot-delete";
    public static final String INTEGRITY_VIOLATION = "integrity-violation";
    public static final String MAX_LENGTH_MSG_CODE = "validator.max.length";
    public static final String MIN_NUMBER_MSG_CODE = "validator.number.min";
    /**
     * ファイル名の長さは{0}以下でなければなりません。
     */
    public static final String MESSAGE_E1003 = "E1003";
    /**
     * ファイルサイズは{0}以下でなければなりません。
     */
    public static final String MESSAGE_E1004 = "E1004";
    /**
     * ファイルがすでに存在しています。ファイル名を変更して、再度アプロードしてください。
     */
    public static final String MESSAGE_E1007 = "E1007";
    /**
     * すでに存在します。
     */
    public static final String MESSAGE_E1008 = "E1008";
    /**
     * 無効なファイル情報
     */
    public static final String MESSAGE_E9996 = "E9996";
    /**
     * 操作権限はありません。
     */
    public static final String MESSAGE_E9997 = "E9997";
    /**
     * 他サービスとの通信が失敗しました。
     */
    public static final String MESSAGE_E9998 = "E9998";
    /**
     * システムエラーが発生しました。しばらく時間をおいてから再度お試しください。
     */
    public static final String MESSAGE_E9999 = "E9999";

    /**
     * コンストラクタ
     */
    private MessageIdConstants() {
    }

}
