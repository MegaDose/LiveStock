package com.bongi.emobilepastoralism.data;

public class NotificationItem {
    private int id, read ,type  ,count;
	private String mail, details, name ,notepadPic, time ,amp, file;

	public NotificationItem() {
	}

	public NotificationItem(int id, int read, int type, String file, int count, String mail, String amp, String details, String name, String notepadPic, String time) {
		super();
		this.id = id;
        this.read =read;
		this.type = type;
        this.file = file;
		this.mail =mail;
        this.amp =amp;
        this.details = details;
        this.name =name;
        this.notepadPic = notepadPic;
        this.time = time;
        this.count =count;

	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

	public String getMial() {
		return mail;
	}

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAmp() {
        return amp;
    }

    public void setAmp(String amp) {
        this.amp = amp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotepadPic() {
        return notepadPic;
    }

    public void setNotepadPic(String notepadPic) {
        this.notepadPic = notepadPic;
    }

    public String getTimeStamp() {
        return time;
    }

    public void setTimeStamp(String time) {
        this.time = time;
    }


}
