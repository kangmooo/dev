package main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * pam_tag_mapping Entity
 *
 * @author SungTae, Kang
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PamTagMapping {

	private Long pam_tag_mapping_seq;
	private Long pam_mapping_seq;
	private Integer order_by;
	private Long calculate_order;
	private String calculate_expression;
	private String tag_alias;
	private Double threshold_actual_high;
	private Double threshold_actual_low;
	private Double threshold_posi;
	private Double threshold_nega;
	private String incident_list;
	private Timestamp creation_date;
    private Timestamp first_date;
    private Character update_flag;
	private List<Integer> incidentSeqList;
}
