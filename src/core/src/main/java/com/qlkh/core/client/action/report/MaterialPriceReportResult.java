package com.qlkh.core.client.action.report;

import net.customware.gwt.dispatch.shared.Result;

/**
 * The Class MaterialMissingPriceReportResult.
 *
 * @author Nguyen Duc Dung
 * @since 5/23/13 1:29 PM
 */
public class MaterialPriceReportResult implements Result {

    private String reportUrl;

    public MaterialPriceReportResult() {
    }

    public MaterialPriceReportResult(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }
}
