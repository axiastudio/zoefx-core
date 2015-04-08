/*
 * Copyright (c) 2014, AXIA Studio (Tiziano Lattisi) - http://www.axiastudio.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the AXIA Studio nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AXIA STUDIO ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL AXIA STUDIO BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.axiastudio.zoefx.desktop.model.converters;

import javafx.util.Callback;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * User: tiziano
 * Date: 17/12/14
 * Time: 16:07
 */
public class String2BigDecimal implements Callback<String, BigDecimal> {
    @Override
    public BigDecimal call(String param) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        String symbol = numberFormat.getCurrency().getSymbol();
        String regex = "-?("+symbol+" )?[0-9]{1,3}(\\.?[0-9]{3})*(,[0-9]{2})?";
        if (!Pattern.matches(regex, param)) {
            return null;
        }
        try {
            Number number = numberFormat.parse(param);
            if (number instanceof Double) {
                // es. "€ 12,99"
                return new BigDecimal((Double) number);
            } else if (number instanceof Long) {
                // es. "€ 12"
                return new BigDecimal((Long) number);
            }
        } catch (ParseException e) {
            // es. "12,0"
            return new BigDecimal(Double.parseDouble(param.replace(",", ".")));
        } catch (ClassCastException e) {
            return null;
        }
        return null;
    }
}
