package lxt.message.req;

public class VoiceMessage extends BaseMessage {
	// √ΩÃÂID
	private String MediaId;
	// ”Ô“Ù∏Ò Ω
	private String Format;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}
}
