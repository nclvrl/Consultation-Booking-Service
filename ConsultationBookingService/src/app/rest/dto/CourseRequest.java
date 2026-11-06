package app.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CourseRequest {

	@NotBlank @Size(max = 20)
	private String code;
	@NotBlank @Size(max = 150)
	private String title;

	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
}
